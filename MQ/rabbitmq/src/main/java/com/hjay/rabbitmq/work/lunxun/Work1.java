package com.hjay.rabbitmq.work.lunxun;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Work1 {
    public static void main(String[] args) {
        //        1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
//        2.创建链接connect
            connection = connectionFactory.newConnection("work1-消费者");
//        3.通过链接获取通道channel
            channel = connection.createChannel();
//        4.通过创建交换机，创建队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "queue1";
            channel.basicConsume(queueName, true, (s, delivery) -> {
                try {
                    System.out.println("work1-消费者收到消息" + new String(delivery.getBody(), StandardCharsets.UTF_8));
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, s -> System.out.println("消息接收失败"));
        } catch (Exception e) {
            System.out.println("出错了" + e);
        } finally {
//        7.关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    System.out.println("出错了" + e);
                }
            }
//        8.关闭链接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println("出错了" + e);
                }
            }
        }
    }
}
