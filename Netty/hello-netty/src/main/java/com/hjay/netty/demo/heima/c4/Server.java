package com.hjay.netty.demo.heima.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugRead;

/**
 * 阻塞模式服务端
 */
@Slf4j
public class Server {

    public static void main(String[] args) {
        // nio阻塞模式
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16);

            // 1.创建服务器
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            serverSocketChannel.configureBlocking(false);// 默认为true，表示是阻塞模式

            // 2.绑定端口
            serverSocketChannel.bind(new InetSocketAddress(8080));

            // 3.连接集合
            List<SocketChannel> channels = new ArrayList<>();
            while (true){
                // 4.与客户端进行通信
                log.info("正在连接......");
                SocketChannel accept = serverSocketChannel.accept();// 阻塞方法，获取客户端连接
                if (accept != null){// 改为非阻塞模式后，进行判断
                    log.info("连接完成");
//                    accept.configureBlocking(false);// 默认为true，表示阻塞模式
                    channels.add(accept);
                }

                // 5.接收客户端发送的数据
                for (SocketChannel channel:channels){
                    log.info("开始读取数据=>" + channel);
                    int read = channel.read(buffer);// 读数据也是阻塞,当设置为非阻塞后，就不会阻塞，如果没有读到数据，返回0
                    if (read > 0){
                        buffer.flip();// 切换读模式
                        debugRead(buffer);
                        buffer.clear();
                        log.info("数据读取完成=>" + channel);
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
