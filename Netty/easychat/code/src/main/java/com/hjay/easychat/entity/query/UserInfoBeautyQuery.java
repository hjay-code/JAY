package com.hjay.easychat.entity.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoBeautyQuery {

    private Integer id;

    private String email;

    private String user_id;

    private Integer status;
}
