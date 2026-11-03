package com.hjay.netty.demo.netty.protocol;

import com.hjay.netty.demo.netty.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 自定义消息编解码器
 * 魔数
 * 版本号
 * 序列化算法 jdk， json， protobuf， hessian
 * 指令类型
 * 请求序号
 * 正文长度
 * 消息正文
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        // 1.魔数
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});

        // 2.版本
        byteBuf.writeByte(1);

        // 3.序列化算法 使用1字节代表序列化方式
        byteBuf.writeByte(0);// 假设0代表jdk算法

        // 4.指令类型，使用1字节代表
        byteBuf.writeByte(message.getMessageType());

        // 5.请求序号，使用4字节
        byteBuf.writeInt(message.getSequenceId());
        // 添加一个无意义字节，用于填充，将消息序列头变为2的整数倍
        byteBuf.writeByte(0xff);

        // 将消息对象进行序列化操作
        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bass);
        oos.writeObject(message);
        byte[] bytes = bass.toByteArray();

        // 6.消息长度
        byteBuf.writeInt(bytes.length);

        // 7.消息正文
        byteBuf.writeBytes(bytes);

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 1.获取魔数
        int magicNum = byteBuf.readInt();

        // 2.获取版本
        byte version = byteBuf.readByte();

        // 3.序列化算法
        byte serializerType = byteBuf.readByte();

        // 4.指令类型
        byte messageType = byteBuf.readByte();

        // 5.消息队列序号
        int sequenceId = byteBuf.readInt();

        // 填充的无意义编码
        byteBuf.readByte();

        // 6.消息长度
        int length = byteBuf.readInt();

        // 7.消息正文
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);

        // 消息内容反序列化
        if (serializerType == 0){// jdk序列化算法
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message = (Message) objectInputStream.readObject();
            log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
            log.debug("{}", message);

            // 解码的消息需要添加到list中，一边传递到下一层
            list.add(message);
        }
    }
}
