package com.hjay.netty.demo.netty.protocol;

import com.hjay.netty.demo.netty.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * 消息编解码器测试类
 */
public class TestMessageCodes {

    public static void main(String[] args) throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(
                // 添加帧解码器用来防止黏包半包现象
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodec());

        // 测试自定义消息编解码器的encode方法
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(loginRequestMessage);

        // decode方法
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginRequestMessage, byteBuf);
        channel.writeInbound(byteBuf);
    }
}
