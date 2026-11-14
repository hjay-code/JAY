package com.hjay.demo.service;

import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    /**
     * 使用 WebClient
     * @param city
     * @return
     */
    public String weather(String city){
        String apiKey = "9cb096a77d1901229825322500fe6a13";
        String url = "https://apis.juhe.cn/simpleWeather/query?city="+city+"&key="+apiKey;
        Mono<String> mono = WebClient.create()
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);

        return mono.block();
    }


    /**
     * 使用 httpInterface
     * @param city
     * @return
     */
    public String getWeather(String city){
        String apiKey = "9cb096a77d1901229825322500fe6a13";
        String url = "https://apis.juhe.cn";

        // 创建客户端
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .codecs(clientCodecsBuilder -> {
                    clientCodecsBuilder.defaultCodecs()
                            .maxInMemorySize(256 * 1024 * 1024); // 手动设置缓冲区，防止溢出
                })
                .build();

        // 创建工厂
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        // 获取代理对象
        WeatherInterface weatherInterface = factory.createClient(WeatherInterface.class);

        return weatherInterface
                .getWeather(city,
                        apiKey);
    }


    /**
     * 使用config 配置代理对象
     */
    @Autowired
    private WeatherInterface weatherInterfaceProxy;

    public String getWeatherInterface(String city){
        return weatherInterfaceProxy.getWeatherProxy(city, "9cb096a77d1901229825322500fe6a13");
    }
}

