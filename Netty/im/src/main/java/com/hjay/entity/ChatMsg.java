package com.hjay.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 聊天信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg {

    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    private Integer signFlag;

    private Date createTime;
}
