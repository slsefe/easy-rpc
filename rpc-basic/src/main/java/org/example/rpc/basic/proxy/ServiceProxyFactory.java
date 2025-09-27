package org.example.rpc.basic.proxy;

import java.lang.reflect.Proxy;

// 工厂设计模式，创建代理对象
public class ServiceProxyFactory {

    // 根据服务类，创建代理对象
    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
