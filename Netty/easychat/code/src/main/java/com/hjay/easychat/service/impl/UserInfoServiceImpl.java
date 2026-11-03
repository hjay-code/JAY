package com.hjay.easychat.service.impl;

import com.hjay.easychat.entity.enums.BeautyAccountStatusEnum;
import com.hjay.easychat.entity.enums.UserContactTypeEnum;
import com.hjay.easychat.entity.po.UserInfo;
import com.hjay.easychat.entity.po.UserInfoBeauty;
import com.hjay.easychat.entity.query.UserInfoBeautyQuery;
import com.hjay.easychat.entity.query.UserInfoQuery;
import com.hjay.easychat.entity.vo.PaginationResultVO;
import com.hjay.easychat.exception.BusinessException;
import com.hjay.easychat.mappers.UserInfoBeautyMapper;
import com.hjay.easychat.mappers.UserInfoMapper;
import com.hjay.easychat.service.UserInfoService;
import com.hjay.easychat.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户信息service
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoBeautyMapper userInfoBeautyMapper;

    @Override
    public List<UserInfo> findListByParam(UserInfoQuery param) {
        return null;
    }

    @Override
    public Integer findCountByParam(UserInfoQuery param) {
        return null;
    }

    @Override
    public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
        return null;
    }

    @Override
    public Integer add(UserInfo userInfo) {
        return null;
    }

    @Override
    public Integer addBatch(List<UserInfo> userInfoList) {
        return null;
    }

    @Override
    public Integer addOrUpdateBatch(List<UserInfo> userInfoList) {
        return null;
    }

    @Override
    public Integer updateByParam(UserInfo userInfo, UserInfoQuery param) {
        return null;
    }

    @Override
    public Integer deleteByParam(UserInfoQuery userInfoQuery) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return null;
    }

    @Override
    public Integer updateUserInfoByUserId(UserInfo userInfo, String userId) {
        return null;
    }

    @Override
    public Integer deleteUserInfoByUserId(String userId) {
        return null;
    }

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return null;
    }

    @Override
    public Integer updateUserInfoByEmail(UserInfo userInfo, String email) {
        return null;
    }

    @Override
    public Integer deleteUserInfoByEmail(String email) {
        return null;
    }

    @Override
    public void register(String email, String nickName, String password) {
        UserInfo userInfoByEmail = userInfoMapper.getUserInfoByEmail(email);
        if(userInfoByEmail != null){
            throw new BusinessException("邮箱已存在");
        }

        // 获取用户id
        String userId = StringTools.getUserId();

        // 判断靓号
        UserInfoBeauty userInfoByEmail1 = userInfoBeautyMapper.getUserInfoByEmail(email);
        boolean userBeautyBoolean = null != userInfoByEmail1 && BeautyAccountStatusEnum.NO_USE.getStatus().equals(userInfoByEmail1.getStatus());
        if(userBeautyBoolean){
            userId = UserContactTypeEnum.USER.getPrefix() + userInfoByEmail1.getUser_id();
        }

        // 新增用户
        Date curDate = new Date();
        userInfoByEmail = new UserInfo();
        userInfoByEmail.setUser_id(userId);
        userInfoByEmail.setNick_name(nickName);
        userInfoByEmail.setEmail(email);
        userInfoByEmail.setPassword(StringTools.encodeMd5(password));
        userInfoByEmail.setCreate_time(curDate);
        userInfoByEmail.setStatus(UserStatusEnum.ENABLE.getStatus());
        userInfoByEmail.setLast_off_time((int) curDate.getTime());

        userInfoMapper.add(userInfoByEmail);

        // 靓号状态修改
        if (userBeautyBoolean){
            UserInfoBeauty updateBeauty = new UserInfoBeauty();
            updateBeauty.setStatus(BeautyAccountStatusEnum.USEED.getStatus());
            userInfoBeautyMapper.updateUserInfoByUserId(updateBeauty, userInfoByEmail1.getUser_id());
        }

        //TODO 创建机器人好友

    }
}
