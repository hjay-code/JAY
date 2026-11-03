package com.hjay.juc;


import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author HJAY
 * 测试线程的调度
 * 每周四 18:00:00 启动
 */
@Slf4j
public class TestSchedule {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();// 获取当前时间
        log.info("{}", now);

        // 获取周四时间
        LocalDateTime thursday = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        log.info("{}", thursday);

        // 如果当前时间大于周四时间，则将周四时间提前一周mm
        if (now.isAfter(thursday)){
            thursday = thursday.plusWeeks(1);
            log.info("{}", thursday);
        }

        // 计算首次执行延迟时间
        long initialDelay = ChronoField.MILLI_OF_DAY.getFrom(thursday) - ChronoField.MILLI_OF_DAY.getFrom(now);

        try (ScheduledExecutorService pool = Executors.newScheduledThreadPool(1)) {
            /**
             * int initialDelay 首次执行的延迟时间
             * int period 执行周期
             * TimeUnit unit 时间单位
             * 延迟1秒，每1秒执行一次
             */
            pool.scheduleAtFixedRate(() -> {
                log.info("task1");
            }, initialDelay, 7, TimeUnit.DAYS);
        }
    }
}
