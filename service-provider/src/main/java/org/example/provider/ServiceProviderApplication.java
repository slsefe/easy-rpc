package org.example.provider;

import org.example.rpc.basic.server.HttpServer;
import org.example.rpc.basic.server.VertxHttpServer;

public class ServiceProviderApplication {
    public static void main(String[] args) {
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
