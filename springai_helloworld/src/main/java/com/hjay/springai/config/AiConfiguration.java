package com.hjay.springai.config;

import com.hjay.springai.advisor.MySimpleLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        // 创建chatclint对象
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean
    public ChatClient chatClientWithAdvisor(OpenAiChatModel chatClient) {
        // 添加Advisor
        return ChatClient.builder(chatClient).defaultAdvisors(new MySimpleLoggerAdvisor()).build();
    }

    /**
     * 添加自定义提示词模板
     * @param chatClient
     * @return
     */
    @Bean
    public ChatClient chatClientWithDefaultSystem(OpenAiChatModel chatClient) {
        // System Prompt
        String  systemPrompt = """
                你是一个资深的java技术顾问。
                禁止回答任何飞技术类问题，例如天气或者娱乐八卦。
                代码示例必须符合 Java 17+ 规范。
                回答需要符合以下格式： 首先一句话概括问题的核心，然后提示代码示例。最后补充注意事项。
                如果自己不确定，可以说"关于这个问题，我目前没有确切的信息"，禁止编造内容。
                """;
        return ChatClient.builder(chatClient).defaultSystem(systemPrompt).defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }


    /**
     * 会话记忆
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)// 设置消息窗口大小
                .chatMemoryRepository(new InMemoryChatMemoryRepository()) // 设置内存存储
                .build();
    }

    @Bean
    public ChatClient chatClientWithChatMemory(OpenAiChatModel chatClient, ChatMemory chatMemory) {
        return  ChatClient.builder(chatClient)
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()// 添加聊天记录拦截器
                )
                .build();
    }
}
