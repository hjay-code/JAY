package com.hjay.rabbitmq.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Producer {

    public static void main(String[] args) {
//        基于amqp协议，其他中间件基于tcp/ip协议
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
//          需要体检在图形化界面绑定队列
//          需要体检在图形化界面绑定队列
//          需要体检在图形化界面绑定队列
            String exchangeName = "topics_exchange";
            String routing_key = "com.order.test";
            String type = "topics";
//        5.准备消息内容
            String message = "Hello RabbitMQ";
//        6.发送消息给队列queue
            channel.basicPublish(exchangeName, routing_key, null, message.getBytes());
            System.out.println("消息发送成功");
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
