package com.hjay.springai.controller;


import com.hjay.springai.util.DocumentParseUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyRagController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private TokenTextSplitter tokenTextSplitter;

    @Autowired
    private DocumentParseUtil documentParseUtil;


    /**
     * 添加文件到向量库中
     * @return
     */
    @RequestMapping("/add-docs")
    public String addDocs(){
        addChunkDocuments("D:\\HJay\\code\\HJAY\\JAY\\springai_helloworld\\upload\\扫地机器人100问.pdf", "扫地机器人");
        addChunkDocuments("D:\\HJay\\code\\HJAY\\JAY\\springai_helloworld\\upload\\颜色选择.txt", "颜色选择");
        return "ok";
    }

    /**
     * 解析后按照token切分再入库，避免单条Document超过embedding模型输入上限
     * @param filePath
     * @param label
     */
    private void addChunkDocuments(String filePath, String label) {
        List<Document> raw = documentParseUtil.parse(filePath);
        List<Document> chunks = tokenTextSplitter.apply(raw);

        // OpenAI 接口限制单次 input.contents 不超过 20 个
        final int BATCH_SIZE = 20;

        for (int i = 0; i < chunks.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, chunks.size());
            List<Document> batch = chunks.subList(i, end);
            vectorStore.add(batch);
        }

        System.out.println(label + ",向量存储添加完成!(共" + chunks.size() + ")");
    }

    /**
     * 相似性查询
     * @return
     */
    @RequestMapping("/rag-query")
    public String ragQuery(){
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("推荐颜色")
                        .topK(3)
                        .build()
        );
        System.out.println(documents);
        return "ok";
    }

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/ask-rag")
    public String askTag(){
        QuestionAnswerAdvisor answerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .topK(3) // 搜索结果数量
                        .similarityThreshold(0.7) // 搜索相似度，0-1，越大相似度要求越高
                        .build()).build();
        String result = chatClient.prompt()
                .advisors(answerAdvisor)
                .user("推荐颜色")
                .call().content();
        System.out.println(result);
        return result;
    }
}
