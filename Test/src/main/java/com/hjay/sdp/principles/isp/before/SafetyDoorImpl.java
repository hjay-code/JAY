package com.hjay.sdp.principles.isp.before;

import lombok.extern.slf4j.Slf4j;

/**
 * 安全门实现
 */
@Slf4j
public class SafetyDoorImpl implements SafetyDoor {
    @Override
    public void antiTheft() {
        log.info("antiTheft");
    }

    @Override
    public void fireProof() {
        log.info("FireProof");
    }

    @Override
    public void waterProof() {
        log.info("WaterProof");
    }
}
