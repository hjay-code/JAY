package com.hjay.netty.demo.heima.c2;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugAll;

public class TestByteBufferString {

    public static void main(String[] args) {
        // 1.字符串转ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("hello".getBytes());
        debugAll(buffer);
        // 需要先切换读模式才能转成String
        buffer.flip();
        System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());


        // 2.Charset
        ByteBuffer encode = StandardCharsets.UTF_8.encode("hello");
        debugAll(encode);

        CharBuffer decode = StandardCharsets.UTF_8.decode(encode);
        System.out.println(decode.toString());

        // 3.wrap
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
        debugAll(wrap);
    }
}
