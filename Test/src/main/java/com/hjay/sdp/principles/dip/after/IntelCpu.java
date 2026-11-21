package com.hjay.sdp.principles.dip.after;

import lombok.extern.slf4j.Slf4j;

/**
 * CPU类
 */
@Slf4j
public class IntelCpu implements Cpu {

    public void run(){
        log.info("IntelCpu run");
    }
}
