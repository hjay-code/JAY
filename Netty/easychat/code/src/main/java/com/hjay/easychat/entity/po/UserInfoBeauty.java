package com.hjay.easychat.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 靓号表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoBeauty {

    /**
     * 流水号
     */
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户id
     */
    private String user_id;

    /**
     * 状态
     */
    private Integer status;
}
