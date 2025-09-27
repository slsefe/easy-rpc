package org.example.rpc.basic.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 本地注册中心
public class LocalRegistry {
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    // 注册服务
    public static void register(String serviceName, Class<?> clazz) {
        map.put(serviceName, clazz);
    }

    // 获取服务
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
    }

    // 取消注册
    public static void unregister(String serviceName) {
        map.remove(serviceName);
    }
}
