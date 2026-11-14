package com.hjay.demo.service;

import com.hjay.demo.entity.UserEntity;
import com.hjay.demo.event.LoginSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;


/**
 * 事件订阅者
 */
@Service
public class AccountService implements ApplicationListener<LoginSuccessEvent> {

    public void addAccountScore(String username){
        System.out.println("addAccountScore 1 : username="+username);
    }


    /**
     * 实现 接收到事件机制
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        System.out.println("onApplicationEvent==================");
        UserEntity userEntity = (UserEntity) event.getSource();
        addAccountScore(userEntity.getUsername());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
