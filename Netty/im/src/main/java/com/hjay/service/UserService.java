package com.hjay.service;

import com.hjay.entity.User;
import com.hjay.netty.ChatMsg;

import java.util.List;

public interface UserService {

    /**
     * 通过编号查找用户
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 通过用户名查找用户
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    int addUsers(User user);

    /**
     * 保存用户聊天消息
     */
    String saveMsg(ChatMsg chatMsg);

    void updateMsgSigned(List<String> msgIdList);

    /**
     * 更新用户信息
      */
    User updateUserInfo(User user);

    /**
     * 添加好友
     *
     * @param userId
     * @param friendUserName
     * @return
     */
    Integer searchFriendByUserName(String userId, String friendUserName);

    /**
     * 添加好友
     * @param userId
     * @param friendUserId
     * @return
     */
    Integer searchFriendByUserId(String userId, String friendUserId);
}
