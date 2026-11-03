package com.hjay.netty.demo.netty.server.session;

import io.netty.channel.Channel;

/**
 * 会话管理接口
 */
public interface Session {

    /**
     * 绑定会话
     * @param channel
     * @param username
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     * @param channel
     */
    void unbind(Channel channel);

    /**
     * 获取属性
     */
    Object getAttribute(Channel channel, String name);

    /**
     * 设置属性
     * @param channel
     * @param name
     * @param value
     */
    void setAttribute(Channel channel, String name, Object value);

    /**
     * 获取channel
     * @param username
     * @return
     */
    Channel getChannel(String username);
}
