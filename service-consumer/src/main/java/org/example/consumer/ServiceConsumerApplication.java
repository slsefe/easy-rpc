package org.example.consumer;

import org.example.common.model.User;
import org.example.common.service.UserService;

public class ServiceConsumerApplication {

    public static void main(String[] args) {
        // todo 需要获取 UserService 的实现类对象
        UserService userService = null;
        User user = new User();
        user.setUsername("zhangsan");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("newUser.getUsername() = " + newUser.getUsername());
        } else {
            System.out.println("newUser.getUsername() = null");
        }

    }
}
