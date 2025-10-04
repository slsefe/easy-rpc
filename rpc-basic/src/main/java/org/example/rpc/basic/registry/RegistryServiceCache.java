package org.example.rpc.basic.registry;

import org.example.rpc.basic.model.ServiceMetaInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 服务注册本地缓存
public class RegistryServiceCache {

    private final Map<String, List<ServiceMetaInfo>> serviceCaches = new HashMap<>();

    void writeCache(String serviceKey, List<ServiceMetaInfo> newServices) {
        serviceCaches.put(serviceKey, newServices);
    }

    List<ServiceMetaInfo> readCache(String serviceKey) {
        return serviceCaches.getOrDefault(serviceKey, new ArrayList<>());
    }

    void clearCache(String serviceKey) {
        serviceCaches.remove(serviceKey);
    }
}
