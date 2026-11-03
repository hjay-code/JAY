package com.hjay.easychat.service;

import com.hjay.easychat.entity.po.UserInfo;
import com.hjay.easychat.entity.query.UserInfoQuery;
import com.hjay.easychat.entity.vo.PaginationResultVO;

import java.util.List;

public interface UserInfoService {

    /**
     * 根据条件查询列表
     * @param param
     * @return
     */
    List<UserInfo> findListByParam(UserInfoQuery param);

    /**
     * 跟警察条件查询列表
     * @return
     */
    Integer findCountByParam(UserInfoQuery param);

    /**
     * 分页查询
     * @param param
     * @return
     */
    PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param);

    /**
     * 新增
     * @param userInfo
     * @return
     */
    Integer add(UserInfo userInfo);

    /**
     * 批量新增
     * @param userInfoList
     * @return
     */
    Integer addBatch(List<UserInfo> userInfoList);

    /**
     * 批量新增或修改
     * @param userInfoList
     * @return
     */
    Integer addOrUpdateBatch(List<UserInfo> userInfoList);

    /**
     * 多条件更新
     * @param userInfo
     * @param param
     * @return
     */
    Integer updateByParam(UserInfo userInfo, UserInfoQuery param);

    /**
     * 多条件删除
     * @param userInfoQuery
     * @return
     */
    Integer deleteByParam(UserInfoQuery userInfoQuery);

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    UserInfo getUserInfoByUserId(String userId);

    /**
     * 根据用户Id更新用户信息
     * @param userInfo
     * @param userId
     * @return
     */
    Integer updateUserInfoByUserId(UserInfo userInfo, String userId);

    /**
     * 根据用户Id删除用户
     * @param userId
     * @return
     */
    Integer deleteUserInfoByUserId(String userId);

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    UserInfo getUserInfoByEmail(String email);

    /**
     * 根据邮箱更新用户信息
     * @param userInfo
     * @param email
     * @return
     */
    Integer updateUserInfoByEmail(UserInfo userInfo, String email);

    /**
     * 根据邮箱删除用户
     * @param email
     * @return
     */
    Integer deleteUserInfoByEmail(String email);

    /**
     * 注册
     * @param email
     * @param nickName
     * @param password
     */
    void register(String email, String nickName, String password);
}
