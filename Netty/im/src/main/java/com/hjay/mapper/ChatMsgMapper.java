package com.hjay.mapper;

import com.hjay.entity.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMsgMapper {

    int insert(ChatMsg record);
}
