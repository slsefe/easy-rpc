package org.example.rpc.basic.registry;

import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface Registry {
    void init(RegistryConfig config);

    void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    void unregister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void destroy();

    // 心跳检测
    void heartBeat();

    // 服务消费者，监听
    void watch(String serviceKey);

}
