package com.hjay.controller;

import com.hjay.bo.UserBO;
import com.hjay.entity.User;
import com.hjay.enums.SearchFriendsStatusEnum;
import com.hjay.service.UserService;
import com.hjay.util.FastDFSClient;
import com.hjay.util.FileUtils;
import com.hjay.util.IMJSONResult;
import com.hjay.util.MD5Utils;
import com.hjay.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @RequestMapping("/getUser")
    public User getUserById(String id){
        return userService.getUserById(id);
    }


    /**
     * 登录/注册 一体化
     * @param user
     * @return
     */
    @PostMapping("/registerOrlogin")
    public IMJSONResult getUserByUsername(User user){
        User u = userService.getUserByUserName(user.getUsername());
        if(u != null){
            if(!u.getPassword().equals(MD5Utils.getPwd(user.getPassword()))){// 登录
                return IMJSONResult.errorMsg("账号密码不正确!");
            }
        }else {// 注册
            if(userService.addUsers(user) > 0){
                return IMJSONResult.ok("注册成功");
            }else {
                return IMJSONResult.errorMsg("注册失败");
            }
        }
        return IMJSONResult.ok();
    }

    /**
     * 用户上传头像
     * @param userBO
     * @return
     */
    @PostMapping("/uploadFaceBase64")
    public IMJSONResult uploadFaceBase64(UserBO userBO){
        // 获取头像base64字符串
        String base64Data = userBO.getFaceData();
        String userFacePath = "D:\\" + userBO.getUserId() + "userFaceBase64.png";
        // 调用fileUtils类将base64字符串转为文件对象
        try {
            FileUtils.base64ToFile(userFacePath, base64Data);
            MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath);
            if(multipartFile == null){
                System.out.println("base64转文件对象为null");
                return IMJSONResult.errorMsg("上传头像失败，请重新上传");
            }
            // 原始头像地址
            String faceUrl = fastDFSClient.uploadBase64(multipartFile);
            String thump = "_150x150";
            String[] arr = faceUrl.split("\\.");
            // 缩略图地址
            String thumpUrl = arr[0] + thump + ".png";
            // 更新用户对象
            User user = new User();
            user.setId(userBO.getUserId());
            user.setFaceImage(thumpUrl);// 缩略图地址
            user.setFaceImageBig(faceUrl);// 原始头像地址
            User result = userService.updateUserInfo(user);
            if(result == null){
                System.out.println("更新用户头像失败");
                return IMJSONResult.errorMsg("更新用户头像失败，请重新上传");
            }else {
                return IMJSONResult.ok(user);// 返回更新用户数据
            }
        } catch (Exception e) {
            System.out.println("base64转文件对象失败，" + e);
            return IMJSONResult.errorMsg("上传头像失败，请重新上传");
        }
    }

    /**
     * 搜索添加好友
     * @param myUserId
     * @param friendName
     * @return
     */
    @RequestMapping("/searchFriend")
    public IMJSONResult searchFriend(String myUserId, String friendName){
        Integer status = userService.searchFriendByUserName(myUserId,friendName);
        // 未添加好友
        if(status == SearchFriendsStatusEnum.SUCCESS.status){
            User user = userService.getUserByUserName(friendName);
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user,userVo);
            return IMJSONResult.ok(userVo);
        }else{
            // 已添加好友
            String msg = SearchFriendsStatusEnum.getMsgByKey(status);
            return IMJSONResult.errorMsg(msg);
        }
    }
}
