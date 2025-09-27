package org.example.consumer;

import org.example.common.model.User;
import org.example.common.service.UserService;
import org.example.rpc.basic.proxy.ServiceProxyFactory;

public class ServiceConsumerApplication {

    public static void main(String[] args) {
        // 从服务代理工厂获取动态代理对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setUsername("slsefe");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("newUser.getUsername() = " + newUser.getUsername());
        } else {
            System.out.println("newUser.getUsername() = null");
        }

    }
}
