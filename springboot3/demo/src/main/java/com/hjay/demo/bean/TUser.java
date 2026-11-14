package com.hjay.demo.bean;

import lombok.Data;

/**
 * 数据库映射实体类
 */
@Data
public class TUser {

    private long id;
    private String loginName;
    private String nickName;
    private String passwd;
}
