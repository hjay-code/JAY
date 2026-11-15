package com.hjay.mq.kafka.listeners;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * 主题监听器
 */
@Component
public class MyTopicListener {

    @KafkaListener(topics = "tech", groupId = "tech-consumer")
    public void onTech(String message){
        System.out.println("收到消息:" + message);
    }


    @KafkaListener(topics = "news", groupId = "news-consumer")
    public void onNews(ConsumerRecord<?, ?> record){
        String topic = record.topic();
        Object key = record.key();
        Object value = record.value();
        System.out.println(topic + " 收到消息 key: " + key + " value: " + value);
    }

    @KafkaListener(groupId = "news-consumer-all", topicPartitions = {
            @TopicPartition(topic = "news",
                    //partitions = "0", // 偏移量
                    partitionOffsets = {
                    // 单独的分区设置
                    @PartitionOffset(partition = "0", initialOffset = "0")
            })
    })
    public void onNewsALl(ConsumerRecord<?, ?> record){
        String topic = record.topic();
        Object key = record.key();
        Object value = record.value();
        System.out.println(topic + " 收到 All 消息 key: " + key + " value: " + value);
    }
}
