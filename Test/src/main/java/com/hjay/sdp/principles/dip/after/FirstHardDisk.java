package com.hjay.sdp.principles.dip.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 第一个硬盘类
 */
@Slf4j
public class FirstHardDisk implements HardDisk {

    public void save(String data){
      log.info("FirstHardDisk save data:{}", data);
    }

    public String get(){
        log.info("FirstHardDisk get data");
        return "Data";
    }
}
