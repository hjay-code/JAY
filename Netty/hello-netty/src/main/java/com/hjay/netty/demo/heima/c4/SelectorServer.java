package com.hjay.netty.demo.heima.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugRead;

/**
 * selector的服务端
 */
@Slf4j
public class SelectorServer {

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
                    SelectionKey channelKey = channel.register(selector, 0, null);
                    channelKey.interestOps(SelectionKey.OP_READ);// 关注可读事件
                    log.info("{}", channel);
                } else if (key.isReadable()) {// read事件
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        int read = channel.read(buffer); // 如果是正常过断开，read = -1
                        if (read == -1){
                            key.cancel();
                        }else {
                            buffer.flip();
                            debugRead(buffer);
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
