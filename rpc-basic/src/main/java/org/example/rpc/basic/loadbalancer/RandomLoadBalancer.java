package org.example.rpc.basic.loadbalancer;

import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList == null || serviceMetaInfoList.isEmpty()) {
            return null;
        }

        int size = serviceMetaInfoList.size();
        int index = random.nextInt(size);
        return serviceMetaInfoList.get(index);
    }
}
