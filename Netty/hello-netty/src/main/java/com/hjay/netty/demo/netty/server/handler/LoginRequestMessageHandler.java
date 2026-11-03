package com.hjay.netty.demo.netty.server.handler;

import com.hjay.netty.demo.netty.message.LoginRequestMessage;
import com.hjay.netty.demo.netty.message.LoginResponseMessage;
import com.hjay.netty.demo.netty.server.service.UserService;
import com.hjay.netty.demo.netty.server.service.UserServiceFactory;
import com.hjay.netty.demo.netty.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage loginRequestMessage) throws Exception {
        String username = loginRequestMessage.getUsername();
        String password = loginRequestMessage.getPassword();

        UserService userService = UserServiceFactory.getUserService();
        boolean login = userService.login(username, password);

        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(channelHandlerContext.channel(), username);
            message = new LoginResponseMessage(true, "登陆成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码错误");
        }

        channelHandlerContext.writeAndFlush(message);

    }
}
