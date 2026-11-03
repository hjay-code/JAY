package com.hjay.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客户端发送请求，服务端继续响应
 */
public class HelloNettyServer {
    public static void main(String[] args) {
        /**
         * 1.创建一组线程池组
         *  主线程池：接口客户端的请求链接，不做处理
         *  从线程池：主线程池会把任务交给它，从线程池执行任务
         */
        // 主线程池
        EventLoopGroup eventLoopGroup1 = new NioEventLoopGroup();
        // 从线程池
        EventLoopGroup eventLoopGroup2 = new NioEventLoopGroup();

        try {
            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置主从线程，param1：主线程 param2：从线程
            serverBootstrap.group(eventLoopGroup1, eventLoopGroup2)
                    // NIO双向通道
                    .channel(NioServerSocketChannel.class)
                    // 添加子处理器，用户处理从线程池的任务
                    .childHandler(new HelloNettyServerInitializer());

            // 启动服务，并设置端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();

            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            System.out.println("netty服务器开启同步出错!");
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup1.close();
            eventLoopGroup2.close();
        }


    }
}