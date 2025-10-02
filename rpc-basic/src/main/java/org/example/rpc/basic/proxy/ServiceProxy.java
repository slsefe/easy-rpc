package org.example.rpc.basic.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.example.rpc.basic.RpcApplication;
import org.example.rpc.basic.model.RpcRequest;
import org.example.rpc.basic.model.RpcResponse;
import org.example.rpc.basic.serializer.Serializer;
import org.example.rpc.basic.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// JDK动态代理
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(parameterTypes)
                .parameters(args)
                .build();
        try {
            byte[] requestBodyBytes = serializer.serialize(rpcRequest);
            byte[] responseBodyBytes;
            // todo: 从注册中心获取服务地址
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(requestBodyBytes)
                    .execute()) {
                responseBodyBytes = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(responseBodyBytes, RpcResponse.class);
            return rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
