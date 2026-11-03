package com.hjay.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {
    private String senderId;//发送者id
    private String receiverId;//接收者id
    private String msg;//聊天内容
    private String msgId; //用于消息的签收
}
