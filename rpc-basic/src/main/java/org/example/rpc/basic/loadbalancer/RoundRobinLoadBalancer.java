package org.example.rpc.basic.loadbalancer;

import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        int index = this.index.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
