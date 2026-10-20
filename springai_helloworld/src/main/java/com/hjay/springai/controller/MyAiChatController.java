package com.hjay.springai.controller;

import com.hjay.springai.tools.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
public class MyAiChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatClient ollamaChatClient;

    @Autowired
    private ChatClient chatClientWithAdvisor;

    @GetMapping("/ai")
    public String ai(String question){
        return chatClient.prompt()// 创建 Prompt 对象，用于购机按聊天请求
                .user( question) // 设置用户输入
                .call() // 调用模型返回结果
                .content(); //获取返回内容
    }

    @GetMapping("/ai-advisor")
    public String aiAdvisor(String question){
        return chatClientWithAdvisor.prompt()// 创建 Prompt 对象，用于购机按聊天请求
                .user( question) // 设置用户输入
                .call() // 调用模型返回结果
                .content(); //获取返回内容
    }

    @GetMapping(value = "/ai-stream-advisor", produces = "text/html;charset=UTF-8")
    public Flux<String> ollamaChatStreamAdvisor(String question){
        return chatClientWithAdvisor.prompt()
                .user( question)
                .stream()
                .content();
    }

    @GetMapping("/ollama")
    public String ollamaChat(String question){
        return ollamaChatClient.prompt()
                .user( question)
                .call()
                .content();
    }

    @GetMapping(value = "/ollama-stream", produces = "text/html;charset=UTF-8")
    public Flux<String> ollamaChatStream(String question){
        return ollamaChatClient.prompt()
                .user( question)
                .stream()
                .content();
    }


    /**
     * 提示词模板 PromptTemplate
     * @param topic
     * @return
     */
    @GetMapping(value = "/ask", produces = "text/html;charset=UTF-8")
    public Flux<String> ask(String topic){
        PromptTemplate template = new PromptTemplate("介绍下{topic}");
        Prompt prompt = template.create(Map.of("topic", topic));
        return chatClient.prompt(prompt)
                .stream()
                .content();
    }


    /**
     * 提示词模板 PromptTemplate 链式调用
     * @param topic
     * @return
     */
    @GetMapping(value = "/ask-chain", produces = "text/html;charset=UTF-8")
    public Flux<String> askChain(String topic){
        return chatClient.prompt()
                .system("你是要给专业的书评助手")
                .user(u -> u.text("请给我推荐三本关于{topic}的书籍").param("topic", topic))
                .stream()
                .content();
    }


    @Autowired
    private ChatClient chatClientWithDefaultSystem;

    @GetMapping(value = "/ask-default-system", produces = "text/html;charset=UTF-8")
    public Flux<String> askDefaultSystem(String question){
        return chatClientWithDefaultSystem.prompt()
                .user(question)
                .stream()
                .content();
    }


    public record TopicBook(
            // 主题
            String topic,
            // 推荐的书
            List<String> books
    ){ }


    /**
     * 自动转换对象
     * @param topic
     * @return
     */
    @GetMapping(value = "/ask-record", produces = "text/html;charset=UTF-8")
    public String askRecord(String topic){
        TopicBook books = chatClient.prompt()
                .system("你是要给专业的书评助手")
                .user(u -> u.text("请给我推荐三本关于{topic}的书籍").param("topic", topic))
                .call()
                .entity(TopicBook.class);
        System.out.println(books);
        return "ok";
    }


    public record BookReview(
            // 书评人
            String reviewerName,
            // 评分
            int rating,
            // 评价内容
            String comment
    ){}


    @GetMapping(value = "/ask-record-list", produces = "text/html;charset=UTF-8")
    public String askRecordList(){
        List<BookReview> bookReviews = chatClient.prompt()
                .system("你是要给专业的书评助手")
                .user(u -> u.text("请给我{bookName}书的三条评价信息").param("bookName", "Effective Java"))
                .call()
                .entity(new ParameterizedTypeReference<List<BookReview>>() {
                });
        System.out.println(bookReviews);
        return "ok";
    }


    @Autowired
    private ChatClient chatClientWithChatMemory;


    /**
     * 会话记忆
     * @param question
     * @param convId
     * @return
     */
    @GetMapping(value = "/ai-plus", produces = "text/html;charset=UTF-8")
    public String aiPlus(String question, String convId){
        return chatClientWithChatMemory.prompt()
                .user(question)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, convId))
                .call()
                .content();
    }


    @Autowired
    private DateTimeTool dateTimeTool;

    @GetMapping(value = "/ai-tool", produces = "text/html;charset=UTF-8")
    public String aiTool(String question){
        return chatClient.prompt()
                .user(question)
                .tools(dateTimeTool) // 注册工具
                .call()
                .content();
    }
}
