package com.hjay.service;

import com.hjay.entity.MyFriend;
import com.hjay.vo.MyFriendsVO;

import java.util.List;

public interface MyFriendsService {

    /**
     * 是否已添加当前用户为好友
     * @param myFriend
     */
    MyFriend getOneMyFriend(MyFriend myFriend);

    /**
     * 新增好友
     * @param myFriend
     */
    void addMyFriend(MyFriend myFriend);

    /**
     * 获取好友列表
     * @param myUserId
     * @return
     */
    List<MyFriendsVO> getMyFriends(String myUserId);
}
