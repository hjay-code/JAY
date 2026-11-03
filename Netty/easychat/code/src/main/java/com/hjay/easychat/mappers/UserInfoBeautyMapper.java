package com.hjay.easychat.mappers;

import com.hjay.easychat.entity.po.UserInfoBeauty;
import com.hjay.easychat.entity.query.UserInfoBeautyQuery;
import com.hjay.easychat.entity.vo.PaginationResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoBeautyMapper {
    /**
     * 根据条件查询列表
     * @param param
     * @return
     */
    List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery param);

    /**
     * 跟警察条件查询列表
     * @return
     */
    Integer findCountByParam(UserInfoBeautyQuery param);

    /**
     * 分页查询
     * @param param
     * @return
     */
    PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery param);

    /**
     * 新增
     * @param userInfoBeauty
     * @return
     */
    Integer add(UserInfoBeauty userInfoBeauty);

    /**
     * 批量新增
     * @param userInfoList
     * @return
     */
    Integer addBatch(List<UserInfoBeauty> userInfoList);

    /**
     * 批量新增或修改
     * @param userInfoList
     * @return
     */
    Integer addOrUpdateBatch(List<UserInfoBeauty> userInfoList);

    /**
     * 多条件更新
     * @param userInfoBeauty
     * @param param
     * @return
     */
    Integer updateByParam(UserInfoBeauty userInfoBeauty, UserInfoBeautyQuery param);

    /**
     * 多条件删除
     * @param userInfoBeauty
     * @return
     */
    Integer deleteByParam(UserInfoBeauty userInfoBeauty);

    /**
     * 根据用户Id查找用户
     * @param userId
     * @return
     */
    UserInfoBeauty getUserInfoByUserId(String userId);

    /**
     * 根据用户Id更新用户信息
     * @param userInfo
     * @param userId
     * @return
     */
    Integer updateUserInfoByUserId(UserInfoBeauty userInfo, String userId);

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
    UserInfoBeauty getUserInfoByEmail(String email);

    /**
     * 根据邮箱更新用户信息
     * @param userInfo
     * @param email
     * @return
     */
    Integer updateUserInfoByEmail(UserInfoBeauty userInfo, String email);

    /**
     * 根据邮箱删除用户
     * @param email
     * @return
     */
    Integer deleteUserInfoByEmail(String email);
}
