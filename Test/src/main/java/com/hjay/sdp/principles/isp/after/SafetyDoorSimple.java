package com.hjay.sdp.principles.isp.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单防盗门
 */
@Slf4j
public class SafetyDoorSimple implements AntiTheft{
    @Override
    public void antiTheft() {
        log.info("antiTheft");
    }
}
