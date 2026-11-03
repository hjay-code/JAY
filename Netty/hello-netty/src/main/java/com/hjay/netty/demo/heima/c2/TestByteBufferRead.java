package com.hjay.netty.demo.heima.c2;

import java.nio.ByteBuffer;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugAll;
import static com.hjay.netty.demo.heima.util.ByteBufferUtil.getUnsignedByte;

public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读写
//        buffer.get(new byte[4]);
//        debugAll(buffer);
//        buffer.rewind();
//        System.out.println((char) buffer.get());

        // mark&reset
        // mark: 标记，记录position的位置
        // reset： 将position重置到mark的位置
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        buffer.mark();// 加标记，索引’2‘的位置
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        buffer.reset();// 重置到mark的位置
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());

        // get(i) 获取索引位置的值，不改变position
        System.out.println((char) buffer.get(3));
        debugAll(buffer);

    }
}
