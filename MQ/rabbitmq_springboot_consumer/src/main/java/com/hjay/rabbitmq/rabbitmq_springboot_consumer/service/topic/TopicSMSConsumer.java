package com.hjay.rabbitmq.rabbitmq_springboot_consumer.service.topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "sms.topic.queue", durable = "true", autoDelete = "true"),
        exchange = @Exchange(value = "topic_order_exchange", type = ExchangeTypes.TOPIC),
        key = "com.#"
))
public class TopicSMSConsumer {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("sms topic--接收的订单信息是:->" + message);
    }
}
