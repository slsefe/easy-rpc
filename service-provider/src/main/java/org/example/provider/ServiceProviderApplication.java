package org.example.provider;

import org.example.common.service.UserService;
import org.example.rpc.basic.RpcApplication;
import org.example.rpc.basic.registry.LocalRegistry;
import org.example.rpc.basic.server.HttpServer;
import org.example.rpc.basic.server.VertxHttpServer;

public class ServiceProviderApplication {
    public static void main(String[] args) {
        // RPC框架初始化
        RpcApplication.init();

        // 应用启动时，注册提供的服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
