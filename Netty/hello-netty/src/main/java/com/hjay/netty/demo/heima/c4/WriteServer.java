package com.hjay.netty.demo.heima.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);// 注册时设置关注Accept事件
        ssc.bind(new InetSocketAddress(8080));

        while (true){
            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()){
//                    SelectableChannel channel = key.channel();
                    SocketChannel sc = ssc.accept();// 因为serversocketchannel只有一个，所以可以这么写
                    sc.configureBlocking(false);
                    // 模拟向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i< 3000000; i++){
                        sb.append("a");
                    }

                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    while (buffer.hasRemaining()){// 判断buffer中是否还有值
                        int write = sc.write(buffer);// 返回值代表实际写入的值
                        System.out.println("=========="+write);
                    }


                }
            }
        }
    }
}
