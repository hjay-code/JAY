package com.hjay.service.impl;

import com.hjay.entity.MyFriend;
import com.hjay.mapper.MyFriendsMapper;
import com.hjay.service.MyFriendsService;
import com.hjay.vo.MyFriendsVO;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyFriendsServiceImpl implements MyFriendsService {

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private Sid sid;

    @Override
    public MyFriend getOneMyFriend(MyFriend myFriend) {
        return myFriendsMapper.getOneMyFriend(myFriend);
    }

    @Override
    public void addMyFriend(MyFriend myFriend) {
        myFriend.setId(sid.nextShort());
        myFriendsMapper.addMyFriend(myFriend);
    }

    @Override
    public List<MyFriendsVO> getMyFriends(String myUserId) {
        return myFriendsMapper.getMyFriends(myUserId);
    }
}
