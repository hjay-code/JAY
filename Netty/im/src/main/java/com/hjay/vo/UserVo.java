package com.hjay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 清除密码的用户信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private String id;

    private String username;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;
}