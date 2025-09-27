package org.example.rpc.basic;

import org.example.rpc.basic.config.RpcConfig;
import org.example.rpc.basic.constant.RpcConstant;
import org.example.rpc.basic.utils.ConfigUtils;

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
