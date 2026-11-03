package com.hjay.netty.demo.netty.server.service;

/**
 * 用户管理接口
 */
public interface UserService {

    /**
     * 登录
     */
    boolean login(String username, String password);
}
