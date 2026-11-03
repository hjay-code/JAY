package com.hjay.netty.demo.heima.c4;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugAll;
import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugRead;

/**
 * 消息边界的服务端
 */
@Slf4j
public class MessageServer {

    private static void split(ByteBuffer resource){
        resource.flip();
        for (int i=0; i<resource.limit(); i++){
            // 找到一条完整消息
            int length = i + 1 - resource.position();
            if (resource.get(i) == '\n') {
                // 完整消息存入新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从resource向target写
                for (int j=0; j<length; j++){
                    target.put(resource.get());
                }
                debugAll(target);
            }
        }

        resource.compact();
    }

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1.创建selector管理多个channel
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 2.注册到selector中
        // selectionKey 就是事件发生后，通过它，可以知道事件和发生事件的channel
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);
        // 有四种相关事件
//        SelectionKey.OP_ACCEPT;// 连接事件
//        SelectionKey.OP_CONNECT;// 客户端建立连接后出发
//        SelectionKey.OP_READ;// 可读事件
//        SelectionKey.OP_WRITE;// 可写事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);// 表示只关注accept事件
        log.info("register key:{}", selectionKey);

        serverSocketChannel.bind(new InetSocketAddress(8080));

        while (true) {
            // 3.select 方法，没有事件发生，线程阻塞
            selector.select();

            // 4.处理事件，内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();// 获取所有事件的selectionKey
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();// 获取发生事件的key
                iterator.remove();// 处理key时，需要手动删除，防止出现null空指针
                log.info("key:{}", key);
//                key.cancel();// 取消当前事件，也是一种处理方式

                // 5区分事件类型
                if (key.isAcceptable()) {// accept事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);// 设置非阻塞模式
                    // 添加新的selectionKey
                    // 新建一个关联channel的byteBuffer并注册到对应的selector中
                    ByteBuffer channelBuffer = ByteBuffer.allocate(16);
                    SelectionKey channelKey = channel.register(selector, 0, channelBuffer);
                    channelKey.interestOps(SelectionKey.OP_READ);// 关注可读事件
                    log.info("{}", channel);
                } else if (key.isReadable()) {// read事件
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer attachment = (ByteBuffer) key.attachment();// 获取关联的byteBuffer
                        int read = channel.read(attachment); // 如果是正常过断开，read = -1
                        if (read == -1){
                            key.cancel();
                        }else {
                            split(attachment);
                            if (attachment.position() == attachment.limit()){// 需要扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(attachment.capacity() * 2);
                                attachment.flip();
                                newBuffer.put(attachment);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        log.error("客户端断开连接...",e);
                        key.cancel();// 客户端断开连接，从key中删除
                    }
                }

            }
        }
    }
}
