package com.hjay.springai.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

/**
 * 自定义实现callAdvisor和streamAdvisor接口,同时支持两种接口
 */
public class MySimpleLoggerAdvisor implements CallAdvisor, StreamAdvisor {


    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        System.out.println("adviseCall request:" + request.toString());
        ChatClientResponse response = chain.nextCall(request);
        System.out.println("adviseCall response:" + response.toString());
        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        System.out.println("adviseStream request:" + request.toString());
        return chain.nextStream(request).doOnNext(response -> System.out.println("adviseStream response:" + response.toString()));
    }

    @Override
    public String getName() {
        return "简单日志Advisor";
    }


    /**
     * 指定advisor的执行顺序
     * 值越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
