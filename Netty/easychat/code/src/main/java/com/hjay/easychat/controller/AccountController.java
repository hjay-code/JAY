package com.hjay.easychat.controller;

import com.hjay.easychat.entity.constants.Constants;
import com.hjay.easychat.entity.vo.ResponseVO;
import com.hjay.easychat.exception.BusinessException;
import com.hjay.easychat.redis.RedisUtils;
import com.hjay.easychat.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 获取验证码
     * @return
     */
    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(100,43);
        String text = arithmeticCaptcha.text();
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, text, Constants.REDIS_TIME_ONE_MIN * 10);
        String base64 = arithmeticCaptcha.toBase64();
        Map<String, String> map = new HashMap<>();
        map.put("checkCode", base64);
        map.put("checkCodeKey", checkCodeKey);
        return getSuccessResponseVO(map);
    }

    /**
     * 注册
     * @param checkCodeKey
     * @param email
     * @param password
     * @param nickName
     * @param checkCode
     * @return
     */
    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty String password,
                               @NotEmpty String nickName,
                               @NotEmpty String checkCode){

        try {
            if(!checkCode.equalsIgnoreCase((String)redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey))){
                throw new BusinessException("图片验证码不正确");
            }
            userInfoService.register(email, nickName, password);
            return getSuccessResponseVO(null);
        } finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }

    }
}
