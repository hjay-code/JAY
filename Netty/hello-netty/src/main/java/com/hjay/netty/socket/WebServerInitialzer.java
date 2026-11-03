package com.hjay.netty.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocketServer子处理类
 */
public class WebServerInitialzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // WebSocket基于http协议需要的http编解码器
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        // 在http上有一些数据流产生，有大有小，我们需要使用netty对大数据流写提供支持
        pipeline.addLast("ChunkedWriteHandler", new ChunkedWriteHandler());

        // 对httpMessage进行聚合处理，聚合成request或response
        pipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024*64));

        // 处理socket请求，运行在WebSocketServer，设置请求地址
        /**
         * 处理一些繁重复杂的任务
         * 会帮忙处理握手动作:handshaking(close, ping, pong) ping + pong = 心跳
         * 对于websocket， 都是已frams进行传输，不同的数据类型对应的frams也不同
         */
        pipeline.addLast("WebSocketClientProtocolHandler", new WebSocketServerProtocolHandler("/ws"));

        // 自定义handler
        pipeline.addLast("ChatHandler", new ChatHandler());
    }
}
