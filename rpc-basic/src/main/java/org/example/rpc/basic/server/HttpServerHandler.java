package org.example.rpc.basic.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.example.rpc.basic.model.RpcRequest;
import org.example.rpc.basic.model.RpcResponse;
import org.example.rpc.basic.registry.LocalRegistry;
import org.example.rpc.basic.serializer.JdkSerializer;
import org.example.rpc.basic.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// 请求处理器
// 1. 反序列化请求为对象，从请求对象中获取参数
// 2. 根据服务名称从本地注册器中获取实现类
// 3. 根据反射调用方法，得到返回结果
// 4. 对返回结果封装和序列化，写入到响应
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        Serializer serializer = new JdkSerializer();
        System.out.println("received request: " + request.method() + " " + request.uri());
        // 异步处理HTTP请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造响应
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("request is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                // 调用服务实现类，获取结果
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(aClass.newInstance(), rpcRequest.getParameters());
                // 封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("success");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }

            // 响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json; charset=utf-8");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
