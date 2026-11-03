package com.hjay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendsRequest {

    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String requestDateTime;
}
