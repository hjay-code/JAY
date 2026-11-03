package com.hjay.netty.demo.heima.c3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestByteBuf {

    public static void main(String[] args) {
        // 无参默认256，会根据大小自动扩容
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();


        // 自动扩容
        log(buf);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            stringBuilder.append("a");
        }
        buf.writeBytes(stringBuilder.toString().getBytes());
        log(buf);
    }

    // 查看buf自动扩容的方法
    public static void log(ByteBuf buf){
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder stringBuilder = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(stringBuilder, buf);
        System.out.println(stringBuilder.toString());
    }
}
