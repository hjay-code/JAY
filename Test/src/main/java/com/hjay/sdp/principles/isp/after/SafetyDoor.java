package com.hjay.sdp.principles.isp.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 多功能安全门
 */
@Slf4j
public class SafetyDoor implements AntiTheft, FireProof, WaterProof {
    @Override
    public void antiTheft() {
        log.info("antiTheft");
    }

    @Override
    public void fireProof() {
        log.info("fireProof");
    }

    @Override
    public void waterProof() {
        log.info("waterProof");
    }
}
