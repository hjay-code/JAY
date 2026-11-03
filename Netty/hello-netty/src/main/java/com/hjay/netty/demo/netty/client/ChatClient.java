package com.hjay.netty.demo.netty.client;

import com.hjay.netty.demo.netty.message.*;
import com.hjay.netty.demo.netty.protocol.MessageCodexSharable;
import com.hjay.netty.demo.netty.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodexSharable MESSAGE_CODEC = new MessageCodexSharable();
        // 倒计时锁，当为0时继续
        CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
        // 登录标志:暂未登录
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        // 是否退出
        AtomicBoolean EXIT = new AtomicBoolean(false);

        Scanner scanner = new Scanner(System.in);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new ProcotolFrameDecoder());
                    socketChannel.pipeline().addLast(LOGGING_HANDLER);
                    socketChannel.pipeline().addLast(MESSAGE_CODEC);

                    // 用来判断读空闲时间和写空闲时间是否过长，防止连接假死
                    // 三个参数，读空闲时间，写空闲时间，读和写的空闲时间
                    // 3s内如果没有向服务器写数据，会触发事件，需要自定义
                    // 一般服务器的读空闲时间是客户端写服务器时间的两倍
                    socketChannel.pipeline().addLast(new IdleStateHandler(0,3, 0));
                    // 这个handler可以同时作为入站和出站处理器
                    socketChannel.pipeline().addLast(new ChannelDuplexHandler(){
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.WRITER_IDLE){// 触发写空闲事件
                                //log.debug("3s 没有写数据了，发送一个心跳包");
                                ctx.writeAndFlush(new PingMessage());
                            }
                        }
                    });

                    // 添加登录请求handler
                    socketChannel.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter(){

                        // 接受响应事件
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("msg:{}", msg);
                            // 处理登录请求响应
                            if (msg instanceof LoginResponseMessage responseMessage){
                                if (responseMessage.isSuccess()) {
                                    LOGIN.set(true);
                                }
                            }
                            WAIT_FOR_LOGIN.countDown();
                        }

                        // 建立连接时发送登录请求
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 负责接受用户在控制台的输入，向server发送各种各样的消息
                            // 这里新开一个线程是为了防止占用netty的nio线程和java的system.in
                            new Thread(() -> {

                                System.out.println("请输入用户名:");
                                String username = scanner.nextLine();
                                if(EXIT.get()){
                                    return;
                                }
                                System.out.println("请输入密码:");
                                String password = scanner.nextLine();
                                if(EXIT.get()){
                                    return;
                                }


                                // 构造消息对象
                                LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);

                                // 发送消息
                                ctx.writeAndFlush(loginRequestMessage);

                                System.out.println("等待");
                                try {
                                    WAIT_FOR_LOGIN.await();
                                } catch (InterruptedException e) {
                                    log.error("client error",e);
                                }

                                // 判断登录请求结果
                                if (!LOGIN.get()) {
                                    ctx.channel().close();
                                    return;
                                }

                                // 登录成功
                                while (true){
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("==================================");
                                    String command = null;
                                    try {
                                        command = scanner.nextLine();
                                    } catch (Exception e) {
                                        break;
                                    }
                                    if(EXIT.get()){
                                        return;
                                    }
                                    String[] s = command.split(" ");
                                    switch (s[0]){
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gcreate":
                                            Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                                            set.add(username); // 加入自己
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }

                            }, "system in").start();
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("连接已经断开，按任意键退出..");
                            EXIT.set(true);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            log.debug("连接已经断开，按任意键退出..{}", cause.getMessage());
                            EXIT.set(true);
                        }
                    });
                }
            });

            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
