package org.example.rpc.basic.loadbalancer;

import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.List;

public interface LoadBalancer {

    ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList);
}
