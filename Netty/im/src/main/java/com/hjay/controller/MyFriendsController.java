package com.hjay.controller;

import com.hjay.service.MyFriendsService;
import com.hjay.util.IMJSONResult;
import com.hjay.vo.MyFriendsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ ")
public class MyFriendsController {

    @Autowired
    private MyFriendsService friendsService;

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }


    /**
     * 获取用户好友列表
     * @param userId
     * @return
     */
    @RequestMapping("/myFriends")
    public IMJSONResult getMyFriends(String userId){
        if(StringUtils.isBlank(userId)){
            return IMJSONResult.errorMsg("用户id为空，请重新登录");
        }
        List<MyFriendsVO> myFriends = friendsService.getMyFriends(userId);
        return IMJSONResult.ok(myFriends);
    }
}
