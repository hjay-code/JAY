package com.hjay.netty.demo.heima.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            System.out.println("客户端于服务端连接完成");
            socketChannel.write(StandardCharsets.UTF_8.encode("中国"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
