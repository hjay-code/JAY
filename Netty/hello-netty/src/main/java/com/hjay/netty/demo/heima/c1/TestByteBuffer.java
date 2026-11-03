package com.hjay.netty.demo.heima.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件读取: FileChannel
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        // FileChannel
        try (FileChannel channel = new FileInputStream("D:\\HJay\\Java\\Netty\\hello-netty\\src\\main\\resources\\demo\\data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);// 缓冲区大小
            while (true) {
                // 从channel读取数据写入buffer
                int read = channel.read(buffer);// 返回读取的字节数
                log.info("读取到的字节数{}", read);
                if (read == -1){// -1表示没有内容可读
                    break;
                }
                // 打印buffer内容
                buffer.flip();// 切换到读模式
                while (buffer.hasRemaining()){// 是否还有未读的数据
                    byte b = buffer.get();// 默认每次读一个字节
                    log.info("读取到的字节{}", (char) b);
                }

                // 切换为写模式
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
