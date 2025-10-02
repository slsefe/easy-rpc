package org.example.provider;

import org.example.common.service.UserService;
import org.example.rpc.basic.RpcApplication;
import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.config.RpcConfig;
import org.example.rpc.basic.model.ServiceMetaInfo;
import org.example.rpc.basic.registry.Registry;
import org.example.rpc.basic.registry.RegistryFactory;
import org.example.rpc.basic.server.HttpServer;
import org.example.rpc.basic.server.VertxHttpServer;

public class ServiceProviderApplication {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 应用启动时，注册服务到注册中心
        String userServiceName = UserService.class.getName();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                .serviceName(userServiceName)
                .serviceHost(rpcConfig.getServerHost())
                .servicePort(rpcConfig.getServerPort())
                .build();
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(String.format("向注册中心{%s}注册服务{%s}失败", registryConfig.getRegistry(), userServiceName), e);
        }

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}
