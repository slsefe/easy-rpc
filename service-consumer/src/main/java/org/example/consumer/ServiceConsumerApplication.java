package org.example.consumer;

import org.example.common.model.User;
import org.example.common.service.UserService;

public class ServiceConsumerApplication {

    public static void main(String[] args) {
        // 使用静态代理类
        UserService userService = new UserServiceProxy();
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
