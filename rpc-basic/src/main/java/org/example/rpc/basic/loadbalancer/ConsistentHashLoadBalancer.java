package org.example.rpc.basic.loadbalancer;

import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基于一致性哈希算法的负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    // 一致性哈希环，存放虚拟节点
    // 基于TreeMap实现，有序map
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    // 虚拟节点数量
    public static final int VIRTUAL_NODE_COUNT = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }

        // 构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                String key = serviceMetaInfo.getServiceAddress() + "#" + i;
                virtualNodes.put(getHash(key), serviceMetaInfo);
            }
        }

        // 选择最接近且大于等于请求hash值的虚拟节点
        int hashCode = getHash(requestParams);
        Map.Entry<Integer, ServiceMetaInfo> virtualNode = virtualNodes.ceilingEntry(hashCode);
        if (virtualNode == null) {
            virtualNode = virtualNodes.firstEntry();
        }
        return virtualNode.getValue();
    }

    /**
     * hash算法，可以自行实现
     * @param key
     * @return
     */
    private int getHash(Object key) {
        return key.hashCode();
    }
}
