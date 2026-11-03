package com.hjay.mapper;


import com.hjay.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {

    User getUserById(String id);

    User getUserByUserName(String userName);

    int addUser(User user);

    int updateUserInfo(User user);
}
