package com.hjay.netty.demo.heima.c1;

import java.nio.ByteBuffer;

/**
 * 动态分区测试类
 */
public class TestByteBufferAllocate {

    public static void main(String[] args) {
        // java堆内存
        // 收到gc的影响
        System.out.println(ByteBuffer.allocate(16).getClass());

        // 直接内存
        // 读写效率较高，少一次拷贝
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
