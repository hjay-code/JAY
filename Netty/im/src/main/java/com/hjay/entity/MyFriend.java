package com.hjay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyFriend {

    private String id;

    private String myUserId;

    private String myFriendUserId;

}
