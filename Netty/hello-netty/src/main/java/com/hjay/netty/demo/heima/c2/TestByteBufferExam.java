package com.hjay.netty.demo.heima.c2;

import java.nio.ByteBuffer;

import static com.hjay.netty.demo.heima.util.ByteBufferUtil.debugAll;

/**
 * 网络粘包，半包和拆包样例
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer resource = ByteBuffer.allocate(32);
        resource.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(resource);
        resource.put("w are you?\n".getBytes());
        split(resource);
    }

    private static void split(ByteBuffer resource){
        resource.flip();
        for (int i=0; i<resource.limit(); i++){
            // 找到一条完整消息
            int length = i + 1 - resource.position();
            if (resource.get(i) == '\n') {
                // 完整消息存入新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从resource向target写
                for (int j=0; j<length; j++){
                    target.put(resource.get());
                }
                debugAll(target);
            }
        }

        resource.compact();
    }
}
