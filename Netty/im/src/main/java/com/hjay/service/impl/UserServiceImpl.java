package com.hjay.service.impl;

import com.hjay.entity.MyFriend;
import com.hjay.entity.User;
import com.hjay.enums.MsgSignFlagEnum;
import com.hjay.enums.SearchFriendsStatusEnum;
import com.hjay.mapper.ChatMsgMapper;
import com.hjay.mapper.MyFriendsMapper;
import com.hjay.mapper.UserMapperCustom;
import com.hjay.mapper.UsersMapper;
import com.hjay.netty.ChatMsg;
import com.hjay.service.UserService;
import com.hjay.util.FastDFSClient;
import com.hjay.util.FileUtils;
import com.hjay.util.MD5Utils;
import com.hjay.util.QRCodeUtils;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private UserMapperCustom userMapperCustom;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Override
    public User getUserById(String id) {
        return usersMapper.getUserById(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return usersMapper.getUserByUserName(userName);
    }

    @Override
    public int addUsers(User user) {
        user.setNickName("和大帅");
        // 为每个用户生成一个唯一的二维码
        String qrCodePath = "D\\user" + user.getId() + "qrcode.png";
        // 二维码对象信息
        qrCodeUtils.createQRCode(qrCodePath, "bird_qrcode:" + user.getUsername());
        MultipartFile multipartFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = null;
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(multipartFile);
        } catch (IOException e) {
            System.out.println("上传头像失败"+ e);
            qrCodeUrl = "";
        }
        user.setQrcode(qrCodeUrl);
        user.setPassword(MD5Utils.getPwd(user.getPassword()));
        user.setFaceImage("");
        user.setFaceImageBig("");
        user.setId(sid.nextShort());
        return usersMapper.addUser(user);
    }

    @Override
    public String saveMsg(ChatMsg chatMsg) {
        com.hjay.entity.ChatMsg msgDB = new com.hjay.entity.ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        userMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Override
    public User updateUserInfo(User user) {
        int result = usersMapper.updateUserInfo(user);
        if(result > 0){
            return usersMapper.getUserById(user.getId());
        }else{
            return null;
        }
    }

    @Override
    public Integer searchFriendByUserName(String userId, String friendUserName) {
        // 查找好友
        User friend = usersMapper.getUserByUserName(friendUserName);
        return searchFriend(userId, friend);
    }

    @Override
    public Integer searchFriendByUserId(String userId, String friendUserId) {
        // 查找好友
        User friend = usersMapper.getUserById(friendUserId);
        return searchFriend(userId, friend);
    }

    /**
     * 判断添加的用户
     * @param userId
     * @param friend
     * @return
     */
    public Integer searchFriend(String userId, User friend){
        //1.搜索的用户如果不存在，则返回【无此用户】
        if(friend == null){
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        //2.搜索的账号如果是你自己，则返回【不能添加自己】
        if(userId.equals(friend.getId())){
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        //3.搜索的朋友已经是你好友，返回【该用户已经是你的好友】
        MyFriend myFriend = myFriendsMapper.getOneMyFriend(new MyFriend(null, userId, friend.getId()));
        if(myFriend != null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }
}
