package com.hjay.netty.demo.heima.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * 零拷贝
 *  切片
 */
@Slf4j
public class TestSlice {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        TestByteBuf.log(buf);

        // 进行切片，切片过程中没有发生数据复制
        // 切片后会对最大容量进行限制，且切片后不能再新增数据
        ByteBuf f1 = buf.slice(0, 5);
        ByteBuf f2 = buf.slice(5, 5);
        TestByteBuf.log(f1);
        TestByteBuf.log(f2);

        log.info("================================================================");
        // 重新赋值，可以判断切片没有发生数据复制
        f1.setByte(0, 'z');

        TestByteBuf.log(buf);
        TestByteBuf.log(f1);

        // 在原有的ByteBuf释放后保存切片的方法
        f1.retain();// 将计数器+1
        // 释放原有的ByteBuf后，切片也就不能使用了
        buf.release();// 这个方法是将计数器-1，想不释放就要将计数器+1
//        log(f1);
    }
}
