package org.example.rpc.basic.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry {
    private Client client;
    private KV kvClient;
    private static final String ETCD_ROOT_PATH = "/rpc/";

    // 本机注册的节点 key 集合（用于维护续期）
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    private RegistryServiceCache serviceCache = new RegistryServiceCache();

    // 正在监听的 key 集合
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    @Override
    public void init(RegistryConfig config) {
        client = Client.builder()
                .endpoints(config.getAddress())
                .connectTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        // 开启心跳检测定时任务
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registerKey = getServiceKey(serviceMetaInfo);
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 添加节点信息到本地缓存中
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(getServiceKey(serviceMetaInfo), StandardCharsets.UTF_8));
        // 服务注销时，从本地缓存中移除节点信息
        localRegisterNodeKeySet.remove(getServiceKey(serviceMetaInfo));
    }


    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 从本地缓存获取
        List<ServiceMetaInfo> cachedServiceMetaInfoList = serviceCache.readCache(serviceKey);
        if (CollectionUtil.isNotEmpty(cachedServiceMetaInfoList)) {
            return cachedServiceMetaInfoList;
        }
        // 如果本地缓存没有，再去注册中心
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();
            List<ServiceMetaInfo> serviceMetaInfoList = kvs.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        // 监听key
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            // 写入缓存
            serviceCache.writeCache(serviceKey, serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    @Override
    public void destroy() {
        // 节点下线
        for (String key : localRegisterNodeKeySet) {
            kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8));
        }
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    /**
     * 定时，对所有集合中的节点重新注册
     */
    @Override
    public void heartBeat() {
        // 10 秒续签一次
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                // 遍历本节点所有的 key
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        // 如果之前未被监听，则开启监听，即对于同一个key，只监听一次
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    // 如果监听的key，出现了删除事件，则清理对应key的服务注册缓存
                    if (Objects.requireNonNull(event.getEventType()) == WatchEvent.EventType.DELETE) {
                        serviceCache.clearCache(serviceNodeKey);
                    }
                }
            });
        }
    }

    private static String getServiceKey(ServiceMetaInfo serviceMetaInfo) {
        return ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
    }
}
