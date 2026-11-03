package com.hjay.netty.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * 用于处理消息
 * 由于传输数据载体为frame，这个frame在netty用于为websocket专门处理文本对象，
 *  frame是消息的载体，类名为TextWebSocketFrame
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /**
     * 用于记录和管理所有客户端的channel
     */
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 获取客户端所传输的消息
        String content = textWebSocketFrame.text();
        System.out.println("接收到的数据" + content);

        // 将数据刷新到所有客户端上
        clients.writeAndFlush(new TextWebSocketFrame(
                "[服务器在 " + LocalDateTime.now()
                + " 接收到消息，消息内容为: " + content + "]"
        ));
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 添加客户端连接
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 移除连接，当客户端断开连接时，会自动移除，可以不写
        // clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长Id为:" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短Id为:" + ctx.channel().id().asShortText());
        System.out.println();
    }
}
