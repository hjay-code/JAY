package com.hjay.netty.demo.netty.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.ProtocolFamily;

/**
 * 自定义一个帧解码器，用来简化代码
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder{

    public ProcotolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
