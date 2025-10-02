package org.example.rpc.basic;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.config.RpcConfig;
import org.example.rpc.basic.constant.RpcConstant;
import org.example.rpc.basic.registry.Registry;
import org.example.rpc.basic.registry.RegistryFactory;
import org.example.rpc.basic.utils.ConfigUtils;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.getConfigValue(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
        // 创建并注册 Shutdown Hook，JVM 退出时销毁注册器
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    // 双重检查锁实现单例
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
