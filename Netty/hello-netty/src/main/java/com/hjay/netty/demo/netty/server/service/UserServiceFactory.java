package com.hjay.netty.demo.netty.server.service;

import com.hjay.netty.demo.netty.server.service.impl.UserServiceMemoryImpl;
import lombok.Getter;

public abstract class UserServiceFactory {

    @Getter
    private static UserService userService = new UserServiceMemoryImpl();

}
