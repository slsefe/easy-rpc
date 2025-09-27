package org.example.rpc.basic.proxy;

import org.example.rpc.basic.RpcApplication;

import java.lang.reflect.Proxy;

// 工厂设计模式，创建代理对象
public class ServiceProxyFactory {

    // 根据服务类，创建代理对象
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy());
    }
}
