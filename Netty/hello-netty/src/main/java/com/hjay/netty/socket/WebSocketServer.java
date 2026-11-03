package com.hjay.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 */
public class WebSocketServer {
    public static void main(String[] args) {
        // 创建主从线程池
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            // 创建服务器类
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 服务器添加主从线程池
            bootstrap.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebServerInitialzer());

            // 绑定端口并设置同步模式
            ChannelFuture future = bootstrap.bind(8088).sync();

            // 关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println("WebSocketServer异常:" + e);
        }finally {
            // 关闭主从线程池
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
