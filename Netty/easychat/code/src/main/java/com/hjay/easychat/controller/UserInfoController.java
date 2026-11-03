package com.hjay.easychat.controller;

import com.hjay.easychat.entity.po.UserInfo;
import com.hjay.easychat.entity.query.UserInfoQuery;
import com.hjay.easychat.entity.vo.ResponseVO;
import com.hjay.easychat.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController{

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据分页条件查询
     * @param param
     * @return
     */
    @RequestMapping("/loadDataList")
    public ResponseVO loadDataList(UserInfoQuery param){
        return getSuccessResponseVO(userInfoService.findListByPage(param));
    }

    /**
     * 新增
     * @param userInfo
     * @return
     */
    @RequestMapping("/add")
    public ResponseVO add(UserInfo userInfo){
        userInfoService.add(userInfo);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增
     * @param list
     * @return
     */
    @RequestMapping("/addBatch")
    public ResponseVO addBatch(@RequestBody List<UserInfo> list){
        userInfoService.addBatch(list);
        return getSuccessResponseVO(null);
    }

    /**
     * 批量新增或修改
     * @param list
     * @return
     */
    @RequestMapping("/addOrUpdateBatch")
    public ResponseVO addOrUpdateBatch(@RequestBody List<UserInfo> list){
        userInfoService.addOrUpdateBatch(list);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据用户Id获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/getUserInfoByUserId")
    public ResponseVO getUserInfoByUserId(String userId){
        return getSuccessResponseVO(userInfoService.getUserInfoByUserId(userId));
    }

    /**
     * 根据用户id修改用户信息
     * @param userInfo
     * @param userId
     * @return
     */
    @RequestMapping("/updateUserInfoByUserId")
    public ResponseVO updateUserInfoByUserId(UserInfo userInfo, String userId){
        return getSuccessResponseVO(userInfoService.updateUserInfoByUserId(userInfo, userId));
    }

    /**
     * 根据用户id删除用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/deleteUserInfoByUserId")
    public ResponseVO deleteUserInfoByUserId(String userId){
        userInfoService.deleteUserInfoByUserId(userId);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据邮箱查找用户信息
     * @param email
     * @return
     */
    @RequestMapping("/getUserInfoByEmail")
    public ResponseVO getUserInfoByEmail(String email){
        return getSuccessResponseVO(userInfoService.getUserInfoByEmail(email));
    }

    /**
     * 根据邮箱修改用户信息
     * @param userInfo
     * @param email
     * @return
     */
    public ResponseVO updateUserInfoByEmail(UserInfo userInfo, String email){
        userInfoService.updateUserInfoByEmail(userInfo, email);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据邮箱删除用户信息
     * @param email
     * @return
     */
    @RequestMapping("/deleteUserInfoByEmail")
    public ResponseVO deleteUserInfoByEmail(String email){
        userInfoService.deleteUserInfoByEmail(email);
        return getSuccessResponseVO(null);
    }
}
