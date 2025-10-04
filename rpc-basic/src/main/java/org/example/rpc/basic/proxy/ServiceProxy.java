package org.example.rpc.basic.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.example.rpc.basic.RpcApplication;
import org.example.rpc.basic.config.RegistryConfig;
import org.example.rpc.basic.config.RpcConfig;
import org.example.rpc.basic.loadbalancer.LoadBalancer;
import org.example.rpc.basic.loadbalancer.LoadBalancerFactory;
import org.example.rpc.basic.model.RpcRequest;
import org.example.rpc.basic.model.RpcResponse;
import org.example.rpc.basic.model.ServiceMetaInfo;
import org.example.rpc.basic.registry.Registry;
import org.example.rpc.basic.registry.RegistryFactory;
import org.example.rpc.basic.serializer.Serializer;
import org.example.rpc.basic.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// JDK动态代理
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造RPC请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        try {
            // 序列化
            byte[] requestBodyBytes = serializer.serialize(rpcRequest);

            // 从配置获取注册中心
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            // 从注册中心获取服务地址
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo(method.getDeclaringClass().getName());
            String serviceKey = serviceMetaInfo.getServiceKey();
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceKey);
            if (serviceMetaInfos.isEmpty()) {
                throw new RuntimeException(String.format("注册器{%s}没有名称为{%s}的服务", registryConfig.getRegistry(), serviceKey));
            }

            // 根据配置的负载均衡算法，选择服务提供者
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 调用方法名（调用路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", method.getName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfos);

            // 发送请求
            byte[] responseBodyBytes;
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
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
