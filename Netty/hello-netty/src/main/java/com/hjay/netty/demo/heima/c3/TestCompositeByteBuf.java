package com.hjay.netty.demo.heima.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

/**
 * 零拷贝
 *  合并，且不会发生数据复制
 */
@Slf4j
public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});

        // 创建一个合并ByteBuf,需要注意ByteBuf的释放问题
        CompositeByteBuf buf = ByteBufAllocator.DEFAULT.compositeBuffer();
//        buf.addComponent(buf1);// 一次加一个
//        buf.addComponents(buf1, buf2);// 一次加多个，但是不会自动调整read和write的指针位置
        // 合并两个ByteBuf,会自动调整read和write指针位置，也不会进行数据复制
        buf.addComponents(true, buf1, buf2);

    }
}
