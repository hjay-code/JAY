package com.hjay.netty.demo.netty.protocol;

import com.hjay.netty.demo.netty.config.Config;
import com.hjay.netty.demo.netty.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义handler如果想要做成可共享，就需要集成另一个类
 * 要求数据传递时不会共享
 * 使用自定义handler处理器时，必须和帧解码器LengthFieldBasedFrameDecoder一起使用
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageCodexSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer();

        // 1.魔数
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});

        // 2.版本
        byteBuf.writeByte(1);

        // 3.序列化算法 使用1字节代表序列化方式
//        byteBuf.writeByte(0);// 假设0代表jdk算法
        byteBuf.writeByte(Config.getSerializerAlgorithm().ordinal());// ordinal中元素enum的序号
        // 4.指令类型，使用1字节代表
        byteBuf.writeByte(message.getMessageType());

        // 5.请求序号，使用4字节
        byteBuf.writeInt(message.getSequenceId());
        // 添加一个无意义字节，用于填充，将消息序列头变为2的整数倍
        byteBuf.writeByte(0xff);

        // 将消息对象进行序列化操作
/*        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bass);
        oos.writeObject(message);
        byte[] bytes = bass.toByteArray();*/

        // 使用枚举，以便后面调用不同的序列化算法
//        byte[] bytes = Serializer.Algorithm.Java.serializer(message);
        // 使用配置类进行读取对应的序列化算法
        byte[] bytes = Config.getSerializerAlgorithm().serializer(message);

        // 6.消息长度
        byteBuf.writeInt(bytes.length);

        // 7.消息正文
        byteBuf.writeBytes(bytes);

        // 这里使用list进行数据传递，就不会进行数据共享
        list.add(byteBuf);
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
/*        if (serializerType == 0){// jdk序列化算法
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Message message = (Message) objectInputStream.readObject();
            log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
            log.debug("{}", message);

            // 解码的消息需要添加到list中，一边传递到下一层
            list.add(message);
        }*/

        // 使用枚举类型进行消息反序列化
//        Message message = Serializer.Algorithm.Java.deserializer(Message.class, bytes);

        // 找到对应的序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerType];
        // 找到对应的消息类型
        Class<? extends Message> messageClass = Message.getMessageClass(messageType);
        Message message = algorithm.deserializer(messageClass, bytes);

        list.add(message);
    }
}
