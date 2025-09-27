package org.example.rpc.basic.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        // 处理请求
        httpServer.requestHandler(new HttpServerHandler());
        // 启动HTTP服务器并监听端口
        httpServer.listen(port);
    }
}
