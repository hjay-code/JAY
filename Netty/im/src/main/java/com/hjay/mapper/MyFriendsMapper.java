package com.hjay.mapper;

import com.hjay.entity.MyFriend;
import com.hjay.vo.MyFriendsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyFriendsMapper {

    MyFriend getOneMyFriend(MyFriend myFriend);

    void addMyFriend(MyFriend myFriend);

    List<MyFriendsVO> getMyFriends(String myUserId);
}
