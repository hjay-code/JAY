package com.hjay.sdp.create.factory.factory_abstract;

import lombok.extern.slf4j.Slf4j;

/**
 * 咖啡
 */
@Slf4j
public abstract class Coffee {

    /**
     * 加糖
     */
    public void addSugar(){
      log.info("addSugar");
    }

    /**
     * 加奶
     */
    public void addMilk(){
      log.info("addMilk");
    }

    abstract String getName();
}
