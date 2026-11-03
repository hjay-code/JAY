package com.hjay.rabbitmq.work.fair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Work2 {
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
            connection = connectionFactory.newConnection("work2-消费者");
//        3.通过链接获取通道channel
            channel = connection.createChannel();
//        4.通过创建交换机，创建队列，绑定关系，路由key，发送消息和接收消息
            String queueName = "queue1";
            Channel finalChannel = channel;
            finalChannel.basicQos(1);// 设置一条消息处理
            // 需要改为手动应答
            finalChannel.basicConsume(queueName, false, (s, delivery) -> {
                try {
                    System.out.println("work2-消费者收到消息" + new String(delivery.getBody(), StandardCharsets.UTF_8));
                    Thread.sleep(200);
                    // 进行应答
                    finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
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
