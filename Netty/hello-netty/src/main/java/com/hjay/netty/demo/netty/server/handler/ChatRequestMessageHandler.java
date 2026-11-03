package com.hjay.netty.demo.netty.server.handler;

import com.hjay.netty.demo.netty.message.ChatRequestMessage;
import com.hjay.netty.demo.netty.message.ChatResponseMessage;
import com.hjay.netty.demo.netty.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        // 接收消息的用户
        String to = chatRequestMessage.getTo();
        Channel toChannel = SessionFactory.getSession().getChannel(to);
        if (toChannel != null){// 消息发送成功
            toChannel.writeAndFlush(new ChatResponseMessage(chatRequestMessage.getFrom(), chatRequestMessage.getContent()));
        }else {// 消息发送失败
            channelHandlerContext.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或不在线"));
        }
    }
}
