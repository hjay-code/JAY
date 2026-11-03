package com.hjay.service;

import com.hjay.entity.FriendsRequest;
import com.hjay.vo.FriendsRequestVO;

import java.util.List;

public interface FriendsRequestService {

    /**
     * 新增好友请求
     * @param friendsRequest
     * @return
     */
    int addFriendRequest(FriendsRequest friendsRequest);

    /**
     * 好友请求列表查询
     * @param acceptUserId
     * @return
     */
    List<FriendsRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 删除好友请求
     * @param friendsRequest
     */
    void deleteFriendRequest(FriendsRequest friendsRequest);

    /**
     * 通过好友请求
     * @param friendsRequest
     */
    void passFriendRequest(FriendsRequest friendsRequest);
}
