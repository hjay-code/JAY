package com.hjay.controller;

import com.hjay.entity.FriendsRequest;
import com.hjay.enums.OperatorFriendRequestTypeEnum;
import com.hjay.enums.SearchFriendsStatusEnum;
import com.hjay.service.FriendsRequestService;
import com.hjay.service.MyFriendsService;
import com.hjay.service.UserService;
import com.hjay.util.IMJSONResult;
import com.hjay.vo.FriendsRequestVO;
import com.hjay.vo.MyFriendsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friendsReuqest")
public class FriendsRequestController {

    @Autowired
    private FriendsRequestService friendsReuqestService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyFriendsService myFriendsService;

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }

    /**
     * 添加好友请求
     * @param friendsRequest
     * @return
     */
    @RequestMapping("/addFriendRequest")
    public IMJSONResult addFriendRequest(FriendsRequest friendsRequest){
        if(StringUtils.isBlank(friendsRequest.getSendUserId()) || StringUtils.isBlank(friendsRequest.getAcceptUserId())){
            return IMJSONResult.errorMsg("好友信息为空");
        }
        // 判断添加好友请求是否成立
        Integer status = userService.searchFriendByUserId(friendsRequest.getSendUserId(), friendsRequest.getAcceptUserId());
        // 可以发送好友请求
        if(status == SearchFriendsStatusEnum.SUCCESS.status){
            friendsReuqestService.addFriendRequest(friendsRequest);
        }else{
            String msg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IMJSONResult.errorMsg(msg);
        }
        return IMJSONResult.ok();
    }

    /**
     * 好友请求列表
     * @param acceptUserId
     * @return
     */
    @RequestMapping("/queryFriendReuqest")
    public IMJSONResult queryFriendRequestList(String acceptUserId){
        List<FriendsRequestVO> friendsRequestVOS = friendsReuqestService.queryFriendRequestList(acceptUserId);
        return IMJSONResult.ok(friendsRequestVOS);
    }

    /**
     * 操作好友请求
     * @param acceptUserId
     * @param sendUserId
     * @param operType
     * @return
     */
    public IMJSONResult operFriendRequest(String acceptUserId, String sendUserId, int operType){
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setAcceptUserId(acceptUserId);
        friendsRequest.setSendUserId(sendUserId);
        if(operType == OperatorFriendRequestTypeEnum.IGNORE.type){
            // 忽略好友请求，则删除此条好友请求
            friendsReuqestService.deleteFriendRequest(friendsRequest);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            // 通过好友请求，则删除此条好友请求时需要新增好友
            friendsReuqestService.passFriendRequest(friendsRequest);
        }else{
            System.out.println("非法操作！");
            return IMJSONResult.errorMsg("非法操作");
        }

        // 查询所有好友列表，返回更新通讯录
        List<MyFriendsVO> myFriends = myFriendsService.getMyFriends(friendsRequest.getAcceptUserId());
        return IMJSONResult.ok(myFriends);
    }
}
