package com.hjay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickName;

    private String qrcode;

    private String cid;
}
