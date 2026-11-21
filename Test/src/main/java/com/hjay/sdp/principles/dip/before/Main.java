package com.hjay.sdp.principles.dip.before;

import lombok.extern.slf4j.Slf4j;

/**
 * 依赖倒转实现
 *  错误实现，只能搭配固定的组件
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        // 创建计算机对象
        Computer computer = new Computer();
        // 创建硬盘
        FirstHardDisk hardDisk = new FirstHardDisk();
        // 创建 内存条
        FirstMemory memory = new FirstMemory();
        // 创建cpu
        IntelCpu cpu = new IntelCpu();

        // 组装电脑
        computer.setCpu(cpu);
        computer.setMemory(memory);
        computer.setHardDisk(hardDisk);

        computer.run();
    }
}
