package com.hjay.netty.demo.heima.c5;

import com.hjay.netty.demo.netty.config.Config;
import com.hjay.netty.demo.netty.message.LoginRequestMessage;
import com.hjay.netty.demo.netty.message.Message;
import com.hjay.netty.demo.netty.protocol.MessageCodexSharable;
import com.hjay.netty.demo.netty.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试消息的序列化算法
 */
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodexSharable CODEC = new MessageCodexSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING,  CODEC, LOGGING);

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123");
        // 出栈
//        channel.writeOutbound(loginRequestMessage);

        // 出栈
        ByteBuf buf = messageToBytes(loginRequestMessage);
        channel.writeInbound(buf);

    }

    /**
     * 测试入栈用的方法
     * 把类转成ByteBuf
     * @param msg
     * @return
     */
    public static ByteBuf messageToBytes(Message msg){
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serializer(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
