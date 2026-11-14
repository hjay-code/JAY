package com.hjay.demo.config;

import com.hjay.demo.convert.YamlHttpMessageConvert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 注入bean来实现 静态资源配置
 */

@Configuration
public class AppConfigStaticResourceBean {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                WebMvcConfigurer.super.addResourceHandlers(registry);
                // 添加自定义规则
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/a/", "classpath:/b/");
            }

            /**
             * 添加自定义消息转换器
             * @param converters initially an empty list of converters
             */
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(new YamlHttpMessageConvert());
                WebMvcConfigurer.super.configureMessageConverters(converters);

            }
        };
    }
}
