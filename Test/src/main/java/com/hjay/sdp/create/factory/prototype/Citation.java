package com.hjay.sdp.create.factory.prototype;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 表彰/奖状
 */
@Data
@Slf4j
public class Citation implements Cloneable{

    // 学生姓名
    private String name;

    @Override
    protected Citation clone() throws CloneNotSupportedException {
        log.debug(" Citation cloning");
        return (Citation) super.clone();
    }

    public void show(){
        log.info("{}同学，获奖", name);
    }
}
