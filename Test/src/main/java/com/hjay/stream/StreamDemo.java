package com.hjay.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class StreamDemo {

    public static void main(String[] args) {

        // 原始数据
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 流 是 懒加载,声明 终止操作 才有有效加载并执行
        // 找到最大值
        list.stream()// 创建流
                .parallel() // 开启并发
                .filter(i -> i % 2 == 0) // 中间操作
                .max(Integer::compareTo) // 终止操作
                .ifPresent(System.out::println); // 判空

    }
}
