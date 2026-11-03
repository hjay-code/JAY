package com.hjay.rabbitmq.rabbitmq_springboot;

import com.hjay.rabbitmq.rabbitmq_springboot.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqSpringbootApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        orderService.makeOrder("1","1",12);
    }

    @Test
    void direct(){
        orderService.makeOrderDirect("1","1", 12);
    }

    @Test
    void topic(){
        orderService.makeOrderTopic("1","1", 12);
    }
}
