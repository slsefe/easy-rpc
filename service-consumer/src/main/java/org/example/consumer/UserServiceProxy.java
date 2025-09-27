package org.example.consumer;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.rpc.basic.model.RpcRequest;
import org.example.rpc.basic.model.RpcResponse;
import org.example.rpc.basic.serializer.JdkSerializer;
import org.example.rpc.basic.serializer.Serializer;

// UserService静态代理类
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();
        // 通过给服务提供者发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class<?>[]{User.class})
                .parameters(new Object[]{user})
                .build();
        try {
            byte[] requestBodyBytes = serializer.serialize(rpcRequest);
            byte[] responseBodyBytes;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                        .body(requestBodyBytes)
                        .execute()) {
                responseBodyBytes = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(responseBodyBytes, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
