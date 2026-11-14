1. 依赖管理机制
1.1. 使用ioc容器来管理组件
1.2. 默认都是单实例组件

2. 自动配置机制 
2.1. 自动扫描启动类目录及其子目录下的组件;可以使用扫描注解指定扫描
2.2. 自动配置类绑定了默认配置文件和属性，如server.port=8080
2.3. spring-boot-autoconfigure 按需加载,配置了绝大多数场景的包，可以自动加载需要的配置
2.4. 使用全注解驱动
2.4.1. @Configuration 注解声明配置工具类AppConfig
2.4.2. 使用方法注解@Bean，可以手动注入组件，默认方法名即为组件名称，可以在方法注解声明自定义组件名称
2.4.3. @Scope("prototype")，可以指定组件状态，这里是多实例，每次都创建一个新的，默认为 singleton
2.4.4. @SpringBootConfiguration 和 @Configuration 没有实质区别
2.4.5. @Import 注解，可以手动指定组件导入。组件名字默认为全类名
2.4.6. @ConditionalOnxxx 条件注解，根据各个场景的情况注入不同的组件。
2.4.6.1. 类注解，对整个类下面的注解都生效
2.4.6.2. 方法注解，只对当前方法生效
2.4.7. 属性绑定类 Pig.class ，需要先注入成组件，然后使用 @ConfigurationProperties(prefix = "pig") 注解来指定配置属性。
2.4.7.1. 要求成员变量必须和配置属性名称一致
2.4.7.2. 也可以在方法注入中使用该注解
2.4.8. @EnableConfigurationProperties(Sheep.class) 在配置类 AppConfigProperties 添加注解可以指定绑定属性类组件，会自动注入到容器中

3. springboot-starter 启动流程
3.1. 导入 starter-web 等场景启动包
3.1.1. 场景启动器导入相关依赖
3.1.2. 场景启动器使用 spring-boot-starter 作为核心启动器
3.1.3. 核心启动器使用 spring-boot-autoconfigure 自动装配官方提供的组件
3.1.4. 默认扫描不到 spring-boot-autoconfigure 下的配置类，根据启动类来决定加载组件
3.2. 启动类添加注解 @SpringBootApplication
3.2.1. 默认指定扫描启动类目录及其子目录下的组件
3.2.2. @EnableAutoConfiguration 注解作用是开启自动配置，使用@Import(AutoConfigurationImportSelector.class)来加载自动配置选择类
3.2.3. 默认从 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.imports 批量导入自动配置工具 xxxAutoConfiguration
3.2.4. xxxAutoConfiguration 也是按需生效，根据条件注解加载组件
3.2.5. 有默认的properties配置文件来绑定属性配置
3.2.6. 只需要在配置文件中重新赋值即可修改绑定的属性
3.3. 编写业务代码

4. 使用组件
4.1. 导入场景启动器 starter（redis)
4.2. 自定义相关场景的配置 @EnableConfigurationProperties(RedisProperties.class) 属性定义在在这里
4.3. RedisAutoConfiguration 自动配置相关组件
4.4. 使用配置的组件
4.5. 可以定制化，自动配置大部分都是没有当前组件时才注入组件，所以对默认组件不满意时可以自定义

5. 配置文件
5.1. properties配置文件
5.2. yaml配置文件

6. 日志配置
6.1. spring5 开始，整合commons-logging 作为接口开放
6.2. springboot3 默认日志为 SLF4j 作为接口， Logback 作为实现
6.3. 核心启动器引用相关
6.4. 日志加载时间比 xxxAutoConfiguration 更早，利用监听器机制在提前配置好，在springboot 的底层包引用
6.5. logging 为日志配置属性
6.6. 日志级别标准 OFF FATAL ERROR WARN INFO DEBUG TRACE ALL， 越往后显示的越详细，默认为info， 一般使用 ERROR 到 INFO
6.7. 自定义配置日志系统，需要配置相关xml文件logback.xml,logback-spring.xml 更推荐的文件名，可以被spring强制管理
6.8. 切换日志框架时，需要先排除掉引用的默认 spring-boot-starter-logging 包
6.9. 添加依赖时，如果第三方依赖添加了其他日志框架，如log4j2，jul等，可以排除掉这些日志框架，默认使用springboot的日志框架即可
6.10. 日志框架可以对接kafka等中间件，在logback-spring.xml中配置即可

7. web 开发
7.1. 使用 spring-boot-starter-web 场景依赖
7.2. 默认配置 视图解析 相关配置
7.3. 默认可以访问静态资源，静态资源默认开启缓存
7.4. 默认配置多种 数据类型转换 和 数据格式化
7.5. 默认 消息转换器（HttpMessageConverters） 可以 自动转换 消息类型
7.6. 默认 国际化 和 消息错误处理机制
7.7. 自动使用 ConfigurableWebBindingInitializer 来对传参进行 消息处理 数据绑定 类型转化 数据校验等功能
7.8. 开发方式 实现一个配置类，实现 WebMvcConfigurer 接口，默认实现类 WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter
7.8.1. 使用默认配置，不做任何修改
7.8.2. 全部自定义 AppConfigMVC ，不使用默认配置，使用 @EnableWebMvc ，会自动禁用默认配置
7.8.3. 不禁用默认配置，添加一些 自定义配置，一般使用这个方式
7.8.4. WebMvcAutoConfiguration 配置类 使用 EnableWebMvcConfiguration 成员来加载所有的 WebMvcConfigurer 实现类
7.8.5. EnableWebMvcConfiguration 继承 DelegatingWebMvcConfiguration 来加载所有的 WebMvcConfigurer
7.8.6. DelegatingWebMvcConfiguration 通过 DI（控制注入） 来注入所有的 WebMvcConfigurer
7.9. spring.web 配置
7.9.1. 国际化 的区域信息
7.9.1.1. classpath:/messages.properties 国际化配置文件
7.9.1.2. classpath:/messages_xx_XX.properties 各个语言的配置
7.9.1.3. 前后端分离的项目中一般由前端控制
7.9.2. 静态资源策略
7.19. spring.mvc 配置静态资源路径前缀等
7.20. 通过配置类 AppConfigStaticResource 来处理静态资源
7.21. HelloController.pathMatch 访问地址允许使用 路径匹配（类似于正则表达式）,在spring.mvc 配置规则
7.22. 内容协商：基于请求头进行内容协商；基于请求参数进行内容协商
7.22.1. 基于请求头进行内容协商 ：Accept 参数代表响应格式， 默认开启
7.22.2. 基于请求参数进行内容协商： format=json 就代表想要响应格式， 默认关闭，需要在 spring.mvc 中开启
7.22.3. 默认支持响应为json， @ResponseBody
7.22.3. 启动器导入了 jackson 依赖，默认可以转成 json
7.22.3.1. DataConvert 类添加注解 @JacksonXmlRootElement  来实现xml转换支持，需要引入 jackson 的 xml 支持的依赖
7.22.4. 可以自定义 内容协商，如返回 yaml 格式或 其他自定义格式，WebMvcConfigurer 提供了 内容协商机制
7.22.4.1. @ResponseBody 由 HttpMessageConverter 返回，使用 dispatcher 找到适配器和handler，获取响应
7.22.4.2. 默认有8种消息转换器，在返回响应时，遍历所有的转换器，找到支持的进行转换 
7.22.4.3. 核心工具类为jackson 的 ObjectMapper
7.22.4.4. 新版的springboot 导入 yaml 依赖后可以自动转换了， Accept:application/yaml 或 ?type=yaml ,浏览器会当成文件下载
7.22.4.4. YamlHttpMessageConvert 自定义 yaml 消息类型抓换器，在 spring.mvc 中添加
7.22.4.5. 在 AppConfigStaticResourceBean（webMvcConfigurer） 中添加消息转换器
7.23. 错误处理流程
7.23.1. 会自适应处理错误，返回 html 或 json
7.23.2. MVC 的错误处理机制保留了，当不能处理时，才使用 spring boot 的机制
7.23.2.1. 添加自定义错误处理机制 ，在 controller 中添加 @ExceptionHandler 方法注解就可以处理当前类的异常
7.23.2.2. @ControllerAdvice 类注解全局异常处理机制，统一处理
7.23.3. springboot 有默认错误处理路径 /error , BasicErrorController 组件， 可以在 server.error.path 中配置自定义的错误路径

8. 嵌入式容器
8.1. Servlet 三大组件，Servlet， Filter， Listener
8.2. 默认使用 Tomcat 作为嵌入式容器
8.3. ioc 容器刷新（启动）onRefresh() 的时候会创建 WebServerFactory 来生成 WebServer
8.4. 使用 server 配置来修改服务器参数 或 自定义 WebServerFactory 来禁用默认的springboot 配置，实现自定义嵌入任意服务器

9. 函数式接口
9.1. AppConfigFunction 配置类添加路由
9.2. 定义路由 userRouter
9.3. 定义handler UserBusiHandler

10. springboot 特性
10.1. 配置文件优先级高于程序设置，jar包外的优先级更高，properties 的优先级更高。越外面的优先级越高
10.2. 支持流式写法
10.3. 环境隔离的profile，可以在添加注解 @Profile 指定生效的环境，在配置文件中指定环境

11. springboot 监听器
11.1. 需要在 META-INF/spring.factories 文件中配置监听器的全路径
11.2. AppListener implements SpringApplicationRunListener 实现生命监听器
11.3. 回调监听器
11.3.1. BootstrapRegistry 引导初始化，在 META-INF/spring.factories 中配置
11.3.2. ApplicationContextInitializer IOC 容器初始化，在 META-INF/spring.factories 中配置
11.3.3. ApplicationListener 监听生命周期，基于事件机制，在 META-INF/spring.factories 中配置 ，使用注解 或 手动注入
11.3.4. SpringApplicationRunListener 监听生命周期， 在 META-INF/spring.factories 中配置，一般使用这个感知全生命周期
11.3.5. ApplicationRunner 监听应用就绪，使用注解，一般项目启动后使用
11.3.6. CommandLineRunner 监听应用就绪，使用注解，一般项目启动后使用
11.4. 九种事件
11.4.1. ApplicationStartingEvent 应用启动但除注册 listener 和 initializers 未做任何事情
11.4.2. ApplicationEnvironmentPreparedEvent 环境准备好，但 context 未创建
11.4.3. ApplicationContextInitializedEvent ApplicationContext 准备完成，ApplicationContextInitializer 执行，bean 未加载
11.4.4. ApplicationPreparedEvent IOC 容器刷新之前，bean 未加载
11.4.5. ApplicationStartedEvent IOC 容器刷新完成， runner 未执行
11.4.6. AvailabilityChangeEvent  LivenessState.CORRECT 应用存活，存活探针====探针机制开始
11.4.7. ApplicationReadyEvent 某一 runner 被调用
11.4.8. AvailabilityChangeEvent  ReadinessState.ACCEPTING_TRAFFIC 应用就绪，就绪探针
11.4.9. ApplicationFailedEvent 启动出错
11.5. 基于事件驱动开发
11.5.1. 事件发布者 EventController
11.5.2. 事件发布工具 implements ApplicationEventPublisherAware 实现springboot事件接口，注入默认的事件发布工具
11.5.3. 事件定义 LoginSuccessEvent extends ApplicationEvent 需要继承 底层事件类型
11.5.4. 事件订阅者1 AccountService implements ApplicationListener<T> 根据泛型来实现获取那种事件的订阅
11.5.5. 事件订阅者2 CouponService 使用方法注解 @EventListener 实现监听
11.5.6. 可以使用注解 @Order 来指定组件加载的顺序来实现监听事件的执行顺序

12. 自定义 starter
12.1. 启动类位置改为自动装配工具类 RobotAutoConfiguration
12.2. 使用 Enable 机制，添加自定义的开启注解 EnableRobot 
12.3. 使用spring的自动加载配置文件，SPI机制， META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports


13. RPC 远程调用 RpcController
13.1. WebClient 非阻塞，响应式 客户端，需要使用 响应式springboot
13.2. HttpService 代理模式，需要使用 响应式springboot
13.2.1. 可以使用配置类 AppConfigWebClient 来简化代理对象的创建，填充默认的请求头等

14. Security 安全框架
14.1. 认证
14.1.1. 提供一个默认表单登录功能
14.1.2. AppConfigSecurity 配置类，可以添加自定义的过滤器，会自动添加到过滤器链，包括自定义登陆规则
14.1.3. 可以在配置文件自定义用户和密码等，但是不够完善，一般都是存储到数据库中
14.1.4. 实现 UserDetailsService 容器 来获取到系统的用户和密码交付给 Security
14.1.5. 需要实现密码加密器 PasswordEncoder 容器
14.1.6. 在配置类开启方法级别的权限控制 EnableWebSecurity 后就可以在其他 SecurityController 方法上添加各种鉴权方法 @PreAuthorize("hasAnyRole('admin')") 类似
14.2. 授权
14.3. 攻击防护
14.4. 基于 Filter 过滤器链