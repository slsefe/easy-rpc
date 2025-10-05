package org.example.rpc.basic.bootstrap;

import org.example.rpc.basic.RpcApplication;
import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.config.RpcConfig;
import org.example.rpc.basic.model.ServiceMetaInfo;
import org.example.rpc.basic.model.ServiceRegistryInfo;
import org.example.rpc.basic.registry.LocalRegistry;
import org.example.rpc.basic.registry.Registry;
import org.example.rpc.basic.registry.RegistryFactory;
import org.example.rpc.basic.server.HttpServer;
import org.example.rpc.basic.server.VertxHttpServer;

import java.util.List;

public class ServiceProviderBootstrap {
    public static void init(List<ServiceRegistryInfo<?>> serviceRegistryInfoList) {
        // RPC框架初始化
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 应用启动时，注册服务到注册中心
        for (ServiceRegistryInfo<?> serviceRegistryInfo : serviceRegistryInfoList) {
            String serviceName = serviceRegistryInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName, serviceRegistryInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = ServiceMetaInfo.builder()
                    .serviceName(serviceName)
                    .serviceHost(rpcConfig.getServerHost())
                    .servicePort(rpcConfig.getServerPort())
                    .build();
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(String.format("向注册中心{%s}注册服务{%s}失败", registryConfig.getRegistry(), serviceName), e);
            }
        }

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(rpcConfig.getServerPort());
    }
}
