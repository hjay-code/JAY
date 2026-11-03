package com.hjay.netty.demo.netty.server.handler;

import com.hjay.netty.demo.netty.message.GroupCreateRequestMessage;
import com.hjay.netty.demo.netty.message.GroupCreateResponseMessage;
import com.hjay.netty.demo.netty.server.session.Group;
import com.hjay.netty.demo.netty.server.session.GroupSession;
import com.hjay.netty.demo.netty.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        // 群聊名称
        String groupName = groupCreateRequestMessage.getGroupName();
        // 群聊初始成员
        Set<String> members = groupCreateRequestMessage.getMembers();

        // 群聊管理
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null){// 不存在当前群聊，创建成功
            // 发送群聊创建成功消息
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));

            // 给群初始成员发送消息，这里只获取在线成员
            List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
            for (Channel channel: membersChannel){
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉去群聊" + groupName));
            }
        }else {// 创建失败
            channelHandlerContext.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "已经存在"));
        }
    }
}
