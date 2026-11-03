package com.hjay.service.impl;

import com.hjay.entity.FriendsRequest;
import com.hjay.entity.MyFriend;
import com.hjay.mapper.FriendsRequestMapper;
import com.hjay.mapper.MyFriendsMapper;
import com.hjay.mapper.UserMapperCustom;
import com.hjay.service.FriendsRequestService;
import com.hjay.service.MyFriendsService;
import com.hjay.vo.FriendsRequestVO;
import com.n3r.idworker.Sid;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FriendsRequestServiceImpl implements FriendsRequestService {

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private UserMapperCustom userMapperCustom;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Override
    public int addFriendRequest(FriendsRequest friendsRequest) {
        //format yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdfdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = sdfdt.format(new Date());// 当前时间
        friendsRequest.setId(sid.nextShort());
        friendsRequest.setRequestDateTime(datetime);
        return friendsRequestMapper.addFriendRequest(friendsRequest);
    }

    @Override
    public List<FriendsRequestVO> queryFriendRequestList(String acceptUserId) {
        return userMapperCustom.queryFriendRequestList(acceptUserId);
    }

    @Override
    public void deleteFriendRequest(FriendsRequest friendsRequest) {
        friendsRequestMapper.deleteFriendRequest(friendsRequest);
    }

    @Override
    public void passFriendRequest(FriendsRequest friendsRequest) {
        // 双向好友保存
        addMyFriend(friendsRequest.getAcceptUserId(), friendsRequest.getSendUserId());
        addMyFriend(friendsRequest.getSendUserId(), friendsRequest.getAcceptUserId());

        // 删除好友请求
        friendsRequestMapper.deleteFriendRequest(friendsRequest);
    }

    /**
     * 添加好友到好友列表
     * @param myUserId
     * @param myFriendUserId
     */
    private void addMyFriend(String myUserId, String myFriendUserId){
        MyFriend myFriend = new MyFriend();
        myFriend.setMyUserId(myUserId);
        myFriend.setMyFriendUserId(myFriendUserId);
        myFriendsMapper.addMyFriend(myFriend);
    }
}
