package com.hjay.sdp.principles.dip.after;

import lombok.extern.slf4j.Slf4j;

/**
 * 依赖倒转实现
 *  修改，使用接口作为组件，可以搭配不同的实现
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        // 创建计算机对象
        Computer computer = new Computer();
        // 创建硬盘
        HardDisk hardDisk = new FirstHardDisk();
        // 创建 内存条
        Memory memory = new FirstMemory();
        // 创建cpu
        Cpu cpu = new IntelCpu();

        // 组装电脑
        computer.setCpu(cpu);
        computer.setMemory(memory);
        computer.setHardDisk(hardDisk);

        computer.run();

    }
}
