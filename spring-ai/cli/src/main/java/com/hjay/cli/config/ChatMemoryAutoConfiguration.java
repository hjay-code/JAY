package com.hjay.cli.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.session.DefaultSessionService;
import org.springframework.ai.session.InMemorySessionRepository;
import org.springframework.ai.session.SessionService;
import org.springframework.ai.session.advisor.SessionMemoryAdvisor;
import org.springframework.ai.session.compaction.RecursiveSummarizationCompactionStrategy;
import org.springframework.ai.session.compaction.SlidingWindowCompactionStrategy;
import org.springframework.ai.session.compaction.TurnCountTrigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryAutoConfiguration {

    /**
     * 会话记忆存储位置
     * 内存
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)// 设置消息窗口大小
                .chatMemoryRepository(new InMemoryChatMemoryRepository()) // 设置内存存储
                .build();
    }


    /**
     * 会话记忆
     * 会话压缩，采用截断方式
     * 长期记忆
     * @return
     */
    @Bean
    SessionMemoryAdvisor sessionMemoryAdvisor() {
        SessionService service = DefaultSessionService.builder()
                .sessionRepository(
                        InMemorySessionRepository.builder().build()
                ).build();
        return SessionMemoryAdvisor.builder(service)
                .defaultUserId("jay")
                // 压缩触发器 会话触发器，设置超过多少论对话触发,当设置为1时
                // 你好 --（获取event 0>1) --> LLM --> 你好 不触发
                // 我是谁 --（获取event 1>1） --> LLM --> 你好 不触发
                // 救救我 --（获取event 2>1） --> LLM -->> 你好 触发
                .compactionTrigger(new TurnCountTrigger(10))
                // 压缩策略，共有四种 策略
                .compactionStrategy(
                        // 按消息数截断，可以设置保留的会话数，1即为只保留当前会话
                        SlidingWindowCompactionStrategy
                                .builder()
                                // 最大会话事件
                                .maxEvents(1)
                                .build()
                )
                .build();
    }

    /**
     * 会话记忆
     * 会话压缩
     * 长期记忆
     * @return
     */
    @Bean
    SessionMemoryAdvisor sessionMemoryAdvisorWithRecursiveSummarization(OpenAiChatModel openAiChatModel) {
        SessionService service = DefaultSessionService.builder()
                .sessionRepository(
                        InMemorySessionRepository.builder().build()
                ).build();
        return SessionMemoryAdvisor.builder(service)
                .defaultUserId("jay")
                // 压缩触发器 会话触发器，设置超过多少论对话触发，当设置为1时
                // 你好 --（获取event 0>1) --> LLM --> 你好 不触发
                // 我是谁 --（获取event 1>1） --> LLM --> 你好 不触发
                // 救救我 --（获取event 2>1） --> LLM -->> 你好 触发
                .compactionTrigger(new TurnCountTrigger(10))
                // 压缩策略，共有四种 策略
                .compactionStrategy(
                        // 除当前策略外，都是截断数据
                        // 大模型生成摘要，不截断数据，而是压缩之前的数据
                        RecursiveSummarizationCompactionStrategy
                                .builder(
                                        ChatClient.builder(openAiChatModel).build()
                                )
                                // 保留不压缩最大会话事件，确保最新的对话数据不被压缩
                                .maxEventsToKeep(10)
                                // 除压缩的数据外需要提供给LLM的数据
                                // 假设有12条数据，
                                // 根据保留的最大值（10） 会压缩第1和第2条数据，
                                // 然后发送数据时会根据设置的额外值（2）额外把第3第4条数据也生成摘要一起发送
                                .overlapSize(2)
                                .build()
                )
                .build();
    }
}
