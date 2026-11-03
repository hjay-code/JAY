package com.hjay.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 清除敏感信息的用户信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBO {
    private  String userId;
    private  String faceData;

}
