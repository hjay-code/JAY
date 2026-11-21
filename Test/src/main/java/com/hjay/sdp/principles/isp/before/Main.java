package com.hjay.sdp.principles.isp.before;

import lombok.extern.slf4j.Slf4j;

/**
 * 接口隔离原则
 *  错误实现，一个接口定义了多个功能，导致实现类并不需要实现多余的功能
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        SafetyDoorImpl safetyDoor = new SafetyDoorImpl();

        safetyDoor.antiTheft();
        safetyDoor.fireProof();
        safetyDoor.waterProof();

        log.info("===========================================");
         // 不需要这么多功能实现
        SafetyDoorSimple safetyDoorSimple = new SafetyDoorSimple();
        safetyDoorSimple.antiTheft();
        safetyDoorSimple.fireProof();
        safetyDoorSimple.waterProof();
    }
}
