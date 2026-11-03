package com.hjay.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRequestVO {

    private String sendUserId;
    private String sendUsername;
    private String sendFaceImage;
    private String sendNickname;

}