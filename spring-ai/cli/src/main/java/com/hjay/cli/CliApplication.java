package com.hjay.cli;

import com.hjay.cli.service.ToolService;
import org.springaicommunity.agent.tools.FileSystemTools;
import org.springaicommunity.agent.tools.ShellTools;
import org.springaicommunity.agent.utils.AgentEnvironment;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallingAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.session.advisor.SessionMemoryAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.util.Scanner;

import static org.springframework.ai.chat.client.ChatClient.*;

@SpringBootApplication
public class CliApplication {

    public static void main(String[] args) {
        SpringApplication.run(CliApplication.class, args);
    }


    /**
     * 命令行
     * @return
     */
    @Bean
    CommandLineRunner commandLineRunner(OpenAiChatModel openAiChatModel,
                                        ChatMemory chatMemory,
                                        SessionMemoryAdvisor sessionMemoryAdvisor,
                                        SessionMemoryAdvisor sessionMemoryAdvisorWithRecursiveSummarization,
                                        ToolService toolService
                                        , ToolCallbackProvider toolCallbackProvider // mcp提供bean
                                        // 默认设置
                                        , @Value("classpath:/prompt/MAIN_AGENT_SYSTEM_PROMPT_V2.md")Resource resource
                                        ) {
        return args -> {

            // 对话代理,添加记忆
            ChatClient chatClient = ChatClient.builder(openAiChatModel)
//                    .defaultSystem(
//                            """
//                                    你是一款命令行交互工具，协助用户完成软件工程相关工作
//                                    """
//                    )
                    // 使用默认的系统设置
                    .defaultSystem(
                            promptSystemSpec -> promptSystemSpec.text(resource)
                                    .param(AgentEnvironment.ENVIRONMENT_INFO_KEY, AgentEnvironment.info())
                                    .param(AgentEnvironment.GIT_STATUS_KEY, AgentEnvironment.gitStatus())
                                    .param(AgentEnvironment.AGENT_MODEL_KEY, openAiChatModel)
                                    // 是否开启知识库，可以穿空字符串或Unknown
                                    .param(AgentEnvironment.AGENT_MODEL_KNOWLEDGE_CUTOFF_KEY, "Unknown")
                    )
                    .defaultAdvisors(
                            // 日志
                            SimpleLoggerAdvisor.builder().build(),
                            // 短期会话记忆
                            // MessageChatMemoryAdvisor.builder(chatMemory).build(),
                            // 长期会话记忆
//                            sessionMemoryAdvisor,
                            sessionMemoryAdvisorWithRecursiveSummarization
                    )  // 添加拦截器
                    .defaultTools(
                            //toolService, // 自定义工具，样例
                            // spring-ai-utils 工具
                            FileSystemTools.builder().build(), // 文件系统工具
                            ShellTools.builder().build()// shell命令工具
                    ) // 添加工具
                    .defaultTools(
                            toolCallbackProvider // mcp server
                    )
                    .build();
            System.out.println("Hello World!");
            System.out.println("我是 jay-cli");
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.print("\n你: ");
                    String userMessage = scanner.nextLine();
                    Flux<String> content = chatClient.prompt()
                            .advisors(a -> a.param(
//                                    ChatMemory.CONVERSATION_ID, "jay",
                                    SessionMemoryAdvisor.SESSION_ID_CONTEXT_KEY, "jay"
                            ))
                            .user(userMessage)
                            .stream().content();
                    System.out.print("\nAI: ");
                    content.doOnNext(System.out::printf).blockLast();

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
