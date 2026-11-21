package com.hjay.sdp.principles.isp.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 接口隔离原则
 *  修改实现，一个接口定义一个功能， 通过 多实现 来规避多余的功能实现
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("=====================简单实现=============================");
        SafetyDoorSimple safetyDoorSimple = new SafetyDoorSimple();
        safetyDoorSimple.antiTheft();

        log.info("=====================多功能实现===============================");
        SafetyDoor safetyDoor = new SafetyDoor();
        safetyDoor.antiTheft();
        safetyDoor.fireProof();
        safetyDoor.waterProof();
    }
}
