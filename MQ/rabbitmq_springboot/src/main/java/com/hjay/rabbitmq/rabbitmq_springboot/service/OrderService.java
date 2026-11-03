package com.hjay.rabbitmq.rabbitmq_springboot.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void makeOrder(String userId, String productId, int num){
        // 1.根据商品id查询库存是否充足
        // 2.保存订单
        String orderId = UUID.randomUUID().toString();
        // 3.消息分发
        /**
         * 交换机
         * 路由key/队列名称 没有交换机时为队列名称
         * 消息内容
         */
        String exchangeName = "fanout_order_exchange";
        String routingKey = "";

        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderId);
    }

    public void makeOrderDirect(String userId, String productId, int num){
        // 1.根据商品id查询库存是否充足
        // 2.保存订单
        String orderId = UUID.randomUUID().toString();
        // 3.消息分发
        /**
         * 交换机
         * 路由key/队列名称 没有交换机时为队列名称
         * 消息内容
         */
        String exchangeName = "direct_order_exchange";

        rabbitTemplate.convertAndSend(exchangeName, "sms", orderId);
        rabbitTemplate.convertAndSend(exchangeName, "email", orderId);
        rabbitTemplate.convertAndSend(exchangeName, "duanxin", orderId);
    }

    public void makeOrderTopic(String userId, String productId, int num){
        // 1.根据商品id查询库存是否充足
        // 2.保存订单
        String orderId = UUID.randomUUID().toString();
        // 3.消息分发
        /**
         * 交换机
         * 路由key/队列名称 没有交换机时为队列名称
         * 消息内容
         */
        String exchangeName = "topic_order_exchange";
//        String routingKey = "com.email.duanxin";
//        String routingKey = "com.email";
        String routingKey = "com.duanxin";
        /**
         *
         * #.duanxin.#
         * *.email.#
         * com.#
         */
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderId);
    }
}
