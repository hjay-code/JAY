package com.hjay.sdp.principles.isp.before;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafetyDoorSimple implements SafetyDoor {
    @Override
    public void antiTheft() {
        log.info("antiTheft");
    }

    @Override
    public void fireProof() {
        log.info("no fireProof");
    }

    @Override
    public void waterProof() {
        log.info("no waterProof");
    }
}
