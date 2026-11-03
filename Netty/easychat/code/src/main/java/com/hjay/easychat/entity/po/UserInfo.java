package com.hjay.easychat.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    /**
     * 用户ID
     */
    private String user_id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nick_name;

    /**
     * 添加方式
     */
    private Integer join_type;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 个性签名
     */
    private String personal_signature;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    /**
     * 最后一次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date last_login_time;

    /**
     * 地区
     */
    private String area_name;

    /**
     * 地区编码
     */
    private String area_code;

    /**
     * 最后一次下线时间
     */
    private Integer last_off_time;

}
