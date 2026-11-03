package com.hjay.netty.demo.heima.advance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServer {

    public static void main(String[] args) {
        new HelloServer().start();
    }

    void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 这个方法可以调整参数，这里设置接受缓冲区为10，也就是系统滑动窗口的大小，一般不用控制
//            serverBootstrap.option(ChannelOption.SO_RCVBUF, 10);
            // 调整netty的接收缓冲区,默认最小是16，每次都是取16的整倍数
            serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //定长消息解码器，设置字节的解析长度
//                    socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(10));

                    //通过分隔符进行消息解码，默认是用换行符，\r\n或\n，也可以使用自定义分隔符
//                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(256));// 默认
//                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(256, ByteBufAllocator.DEFAULT.buffer().writeByte('_')));// 自定义

                    //LTC基于长度字段的消息解码器
                    

                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("server error ", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
