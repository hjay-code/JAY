package com.hjay.springai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeTool {

    /**
     * 工具注解，description 用于大模型识别
     * @return
     */
    @Tool(description = "获取用户在指定时区的当提前日期和时间，用于回答需要实时时间的问题")
    public String getCurrentTime(){
        ZoneId zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        ZonedDateTime now = LocalDateTime.now(zoneId).atZone(zoneId);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
    }


    @Tool(description = "设置闹钟，调用此工具可在指定时间触发提醒。时间参数必须是ISO-86601 格式。例如：2026-05-033 15:30:00")
    public void setAlarm(@ToolParam(description = "闹钟触发时间，标准格式:yyyy-MM-dd HH:mm:ss") String alarmTime){
        System.out.println("闹钟已设置，将在 " + alarmTime + " 提醒用户");
    }
}
