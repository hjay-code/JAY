package com.hjay.demo.config;

import com.hjay.demo.service.WeatherInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfigWebClient {

    @Bean
    public HttpServiceProxyFactory  httpServiceProxyFactory(@Value("${rpc.key}") String key){
        // 创建客户端
        WebClient webClient = WebClient.builder()
                .codecs(clientCodecsBuilder -> {
                    clientCodecsBuilder.defaultCodecs()
                            .maxInMemorySize(256 * 1024 * 1024); // 手动设置缓冲区，防止溢出
                })
                .build();

        // 创建工厂
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return factory;
    }


    @Bean
    public WeatherInterface weatherInterfaceProxy(HttpServiceProxyFactory  httpServiceProxyFactory){
        // 获取代理对象
        WeatherInterface weatherInterface = httpServiceProxyFactory.createClient(WeatherInterface.class);

        return weatherInterface;
    }

}
