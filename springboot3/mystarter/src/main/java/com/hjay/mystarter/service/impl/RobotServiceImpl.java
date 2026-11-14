package com.hjay.mystarter.service.impl;

import com.hjay.mystarter.properties.Robot;
import com.hjay.mystarter.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotServiceImpl implements RobotService {

    @Autowired
    private Robot robot;

    @Override
    public String sayHello() {
        return "hello " + robot.getName();
    }
}
