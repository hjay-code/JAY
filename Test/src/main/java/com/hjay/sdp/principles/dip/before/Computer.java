package com.hjay.sdp.principles.dip.before;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 计算机类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Computer {

    private FirstHardDisk hardDisk;

    private FirstMemory memory;

    private IntelCpu cpu;

    public void run(){
        log.info("Computer run");
        String hardDiskData = hardDisk.get();
        log.info("hardDiskData = {}", hardDiskData);
        memory.save();
        cpu.run();
    }

}
