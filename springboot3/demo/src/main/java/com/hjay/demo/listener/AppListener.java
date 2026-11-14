package com.hjay.demo.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 *
 * 所有的 监听器 都要提前 根据 META-INF/spring.factories 下进行加载
 *
 * springboot 生命周期主要分三步
 * 引导   BootstrapContext 引导
 *      starting    SpringApplication.run ，生成 BootstrapContext 就执行
 *      environmentPrepared     绑定启动参数等到环境变量， IOC 未创建
 *
 * 启动
 *      contextPrepared     IOC 容器创建成功，但是未加载 sources 主配置类，未注册组件
 *      contextLoaded       IOC 容器加载， sources 加载完成，未刷新，自定义组件未注册
 *      started             IOC 容器刷新，自定义组件注册完成，十二步操作
 *      ready               runner 执行完毕
 *      failed              除 starting，感知其余流程的异常
 *
 * 运行
 */
public class AppListener implements SpringApplicationRunListener {

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("==============starting==============正在启动");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("==============environmentPrepared==============环境准备完成");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("==============contextPrepared==============IOC准备完成");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("==============contextLoaded==============IOC加载完成");
    }


    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("==============started==============启动完成");
    }


    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("==============ready==============准备完成");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("==============failed==============应用启动失败");
    }
}
