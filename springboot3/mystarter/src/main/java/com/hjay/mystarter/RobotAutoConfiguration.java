package com.hjay.mystarter;

import com.hjay.mystarter.controller.RobotController;
import com.hjay.mystarter.properties.Robot;
import com.hjay.mystarter.service.impl.RobotServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 自定义自动装配类
 */
@AutoConfiguration
@EnableConfigurationProperties(Robot.class)
@Import({RobotController.class, RobotServiceImpl.class}) // 导入组件（接口不能 @Import，必须导入实现类）
public class RobotAutoConfiguration {


}
