package com.hjay.demo.service;

import com.hjay.demo.entity.UserEntity;
import com.hjay.demo.event.LoginSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * 另一种事件订阅
 * 使用注解
 */
@Service
public class CouponService {

    @EventListener
    public void onEvent(LoginSuccessEvent loginSuccessEvent) {
        System.out.println("onEvent======================");
        UserEntity userEntity = (UserEntity) loginSuccessEvent.getSource();
        sendCoupon(userEntity.getUsername());
    }


    public void sendCoupon(String username){
        System.out.println(username + " get a paper");
    }
}
