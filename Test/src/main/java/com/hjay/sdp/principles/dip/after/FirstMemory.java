package com.hjay.sdp.principles.dip.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 内存条类
 */
@Slf4j
public class FirstMemory  implements Memory {

    public void save(){
        log.info("FirstMemory save");
    }
}
