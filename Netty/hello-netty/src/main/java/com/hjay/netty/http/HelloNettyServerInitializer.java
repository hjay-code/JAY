package com.hjay.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 初始化器，channel注册之后执行相应的初始化方法
 */
public class HelloNettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过SocketChannel获取对应的管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        /**
         * 通过管道添加handler处理类
         * HttpServerCodec: netty提供的处理类，可以当作拦截器
         * 当请求到服务器，我们需要解码，响应到客户端做编码
         */
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        // 添加自定义handler类，给客户端渲染hello-netty
        pipeline.addLast("CustomerHandler", new CustomerHandler());
    }
}
