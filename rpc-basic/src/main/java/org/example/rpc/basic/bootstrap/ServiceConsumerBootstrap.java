package org.example.rpc.basic.bootstrap;

import org.example.rpc.basic.RpcApplication;

/**
 * 服务消费者，启动类
 */
public class ServiceConsumerBootstrap {
    public static void init() {
        // RPC框架初始化
        RpcApplication.init();
    }
}
