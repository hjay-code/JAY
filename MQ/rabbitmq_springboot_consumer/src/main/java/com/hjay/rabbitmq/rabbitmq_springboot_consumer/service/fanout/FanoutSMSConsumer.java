package com.hjay.rabbitmq.rabbitmq_springboot_consumer.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "sms.fanout.queue")
public class FanoutSMSConsumer {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("sms fanout--接收的订单信息是:->" + message);
    }
}
