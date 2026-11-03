package com.hjay.netty.demo.heima.c2;

import java.nio.ByteBuffer;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugAll;

/**
 * 读写模式工具类测试
 */
public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 写模式
        buffer.put((byte) 0x61); // "a"
        debugAll(buffer);

        buffer.put(new byte[]{0x62, 0x63, 0x64});
        debugAll(buffer);

        // 读模式
        buffer.flip();
        System.out.println(buffer.get());
        debugAll(buffer);

        // 写模式
        buffer.compact();
        debugAll(buffer);

        buffer.put(new byte[]{0x65, 0x66});
        debugAll(buffer);
    }
}
