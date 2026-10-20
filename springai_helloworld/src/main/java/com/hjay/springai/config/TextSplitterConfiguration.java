package com.hjay.springai.config;


import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文本分割配置类
 */
@Configuration
public class TextSplitterConfiguration {

    /**
     * token文本分割器
     * @return
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return TokenTextSplitter.builder()
                .withChunkSize(50)// 分割 的最大token数
                .withMaxNumChunks(1000) // 最大可生成的块数量。防止对超长文本无限分割。。打到此上线后即使还有剩余文本也停止分割
                .build();
    }
}
