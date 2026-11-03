package com.hjay.netty.demo.netty.server.handler;

import com.hjay.netty.demo.netty.message.GroupChatRequestMessage;
import com.hjay.netty.demo.netty.message.GroupChatResponseMessage;
import com.hjay.netty.demo.netty.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatRequestMessage groupChatRequestMessage) throws Exception {
        List<Channel> membersChannel = GroupSessionFactory.getGroupSession()
                .getMembersChannel(groupChatRequestMessage.getGroupName());

        // 给在线成员发送消息
        for (Channel channel: membersChannel){
            channel.writeAndFlush(new GroupChatResponseMessage(groupChatRequestMessage.getFrom(), groupChatRequestMessage.getContent()));
        }
    }
}
