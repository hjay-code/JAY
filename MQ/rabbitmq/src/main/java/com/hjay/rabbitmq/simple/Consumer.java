package com.hjay.rabbitmq.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Consumer {
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
            connection = connectionFactory.newConnection("生产者");
//        3.通过链接获取通道channel
            channel = connection.createChannel();
//        4.通过创建交换机，创建队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "queue1";
            channel.basicConsume(queueName, true, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    System.out.println("收到消息" + new String(delivery.getBody(), "UTF-8"));
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("消息接收失败");
                }
            });
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
