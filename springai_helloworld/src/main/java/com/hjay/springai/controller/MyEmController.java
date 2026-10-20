package com.hjay.springai.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MyEmController {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;


    /**
     * 向量
     * @return
     */
    @RequestMapping("/em")
    public String em(){
        float[] embed = embeddingModel.embed("学java上B站");
        System.out.println(embed.length);
        System.out.println(Arrays.toString(embed));
        return "ok";
    }

    @Autowired
    private VectorStore vectorStore;

    /**
     * 向量库 添加
     * @return
     */
    @RequestMapping("/add-doc")
    public String addDoc(){
        List<Document> documents = List.of(
                new Document("学java上b站"),
                new Document("b站学java好"),
                new Document("我爱吃苹果")
        );
        vectorStore.add(documents);
        return "ok";
    }

    /**
     * 向量库 相似性搜索
     * @return
     */
    @RequestMapping("/query")
    public String query(){
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("去哪里学java") // 相似搜索文本
                        .topK(2) // 取两个相似答案
                        .build()
        );
        System.out.println(documents);
        return "ok";
    }
}
