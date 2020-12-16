package com.lianwei.demo04;

import com.lianwei.demo02.UserService;
import com.lianwei.demo02.UserServiceImpl;

public class Client {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        ProxyInvocationHandler pih = new ProxyInvocationHandler();
        pih.setTarget(userService);
        UserService proxy =(UserService) pih.getProxy();

        proxy.delete();
    }
}
