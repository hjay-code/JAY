package com.hjay.rabbitmq.rabbitmq_springboot_consumer.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "sms.direct.queue")
public class DirectSMSConsumer {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("sms direct--接收的订单信息是:->" + message);
    }
}
