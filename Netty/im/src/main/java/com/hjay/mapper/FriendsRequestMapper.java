package com.hjay.mapper;

import com.hjay.entity.FriendsRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendsRequestMapper {

    int addFriendRequest(FriendsRequest friendsRequest);

    void deleteFriendRequest(FriendsRequest friendsRequest);
}
