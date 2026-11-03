package com.hjay.netty.demo.netty.server;

import com.hjay.netty.demo.netty.protocol.MessageCodexSharable;
import com.hjay.netty.demo.netty.protocol.ProcotolFrameDecoder;
import com.hjay.netty.demo.netty.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义handler是否可以使用变量共享，需要看具体环境
 * netty提供的handler一般都在类注解标识了是否可共享，@sharble标识可共享
 * 自定义的handler如果没有数据共享，一般可以定义成变量
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
        QuitHandler QUIT_HANDLER = new QuitHandler();


        // 这里使用可以用作变量的Message
        MessageCodexSharable MESSAGE_CODEC = new MessageCodexSharable();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProcotolFrameDecoder());// 自定义帧解码器
                    socketChannel.pipeline().addLast(LOGGING_HANDLER);// 日志
                    socketChannel.pipeline().addLast(MESSAGE_CODEC);// 消息编解码器

                    // 用来判断读空闲时间和写空闲时间是否过长，防止连接假死
                    // 三个参数，读空闲时间，写空闲时间，读和写的空闲时间
                    // 5s内如果没有收到读数据，会触发事件，需要自定义
                    socketChannel.pipeline().addLast(new IdleStateHandler(5,0, 0));
                    // 这个handler可以同时作为入站和出站处理器
                    socketChannel.pipeline().addLast(new ChannelDuplexHandler(){
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.READER_IDLE){// 触发读空闲事件
                                log.debug("已经5s没有读到数据了");
                                ctx.channel().close();// 关闭客户端链接
                            }
                        }
                    });

                    // 添加handler
                    socketChannel.pipeline().addLast(LOGIN_HANDLER);// 登录
                    socketChannel.pipeline().addLast(CHAT_HANDLER);// 聊天
                    socketChannel.pipeline().addLast(GROUP_CREATE_HANDLER);// 建群
                    socketChannel.pipeline().addLast(GROUP_JOIN_HANDLER);// 加群
                    socketChannel.pipeline().addLast(GROUP_MEMBERS_HANDLER);// 群员
                    socketChannel.pipeline().addLast(GROUP_QUIT_HANDLER);// 退群
                    socketChannel.pipeline().addLast(GROUP_CHAT_HANDLER);// 群聊
                    socketChannel.pipeline().addLast(QUIT_HANDLER);// 断开连接
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
