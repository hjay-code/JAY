package com.hjay.mcp.server.tools;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * MCP-Server 工具
 */
@Component
public class WeatherMcpTool {

    @McpTool(
            name = "getWeatherForecast",
            description = "根据城市名称查询模拟天气预报（模拟数据，非真是气象来源）",
            generateOutputSchema = true
    )
    public String getWeatherForecast(
            @McpToolParam(description = "城市名称，例如：北京", required = true) String city
    ){
        System.out.println("WeatherMcpTool.getWeatherForecast()");
        return city + " 天气：晴天，空气质量：优";
    }
}
