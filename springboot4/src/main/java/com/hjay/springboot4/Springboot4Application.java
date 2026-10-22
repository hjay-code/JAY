package com.hjay.springboot4;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.ImportHttpServices;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.*;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


//  开启多重验证登录
@EnableMultiFactorAuthentication(authorities = {
        FactorGrantedAuthority.PASSWORD_AUTHORITY,
        FactorGrantedAuthority.OTT_AUTHORITY
})
@EnableResilientMethods
@ImportHttpServices(CatFactsClient.class) // 导入CatFactsClient 的 http客户端
@SpringBootApplication
public class Springboot4Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot4Application.class, args);
    }

    /**
     * springboot 6 以后的 leyden机制，用来编译项目，启动很快
     * @return
     */
    @Bean
    JdbcPostgresDialect jdbcPostgresDialect() {
        return JdbcPostgresDialect.INSTANCE;
    }
}

@Configuration
class SecurityConfiguration{

    @Bean
    Customizer<HttpSecurity> httpSecurityCustomizer() {
        return httpSecurity -> httpSecurity
                .webAuthn(a -> a.allowedOrigins("http://localhost:8080/")
                        .rpName("springboot4")
                        .rpId("localhost"))
                .oneTimeTokenLogin(
                otto -> otto.tokenGenerationSuccessHandler(new OneTimeTokenGenerationSuccessHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
                        response.getWriter().println("请查看邮箱");
                        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        IO.println("please go to http://localhost:8080/login/ott?token=" + oneTimeToken.getTokenValue());
                    }
                })
        );
    }


    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        // 设置自动更新用户的密码，更新加密方式
        manager.setEnableUpdatePassword(true);
        return manager;
    }
}


/**
 * 安全组件
 */
@RestController
class MeController{

    @GetMapping("/")
    Map<String, String> me(Principal principal){
        return Map.of("name", principal.getName());
    }
}

/**
 * springboot 7 简化写法
 * 需要在启动类引声明ImportHttpServices
 */
interface CatFactsClient {
    @GetExchange("https://www.catfacts.net/api/")
    CatFacts facts();
}

/**
 * springboot 6 及之前的写法，声明式 httpclient
 */
//@Component
//class CatFactsClient{
//
//    private final RestClient http;
//    public CatFactsClient(RestClient.Builder http) {
//        this.http = http.build();
//    }
//
//    CatFacts facts(){
//        return this.http
//                .get()
//                .uri("https://www.catfacts.net/api/")
//                .retrieve()
//                .body(CatFacts.class);
//    }
//}

record CatFacts(Collection<CatFact> facts){}

record CatFact(String fact) {}


@RestController
class CatController{

    private final AtomicInteger counter = new AtomicInteger();

    private final CatFactsClient client;
    public CatController(CatFactsClient client) {
        this.client = client;
    }


    @GetMapping("/cats")
    CatFacts cats(){
        return client.facts();
    }

    /**
     * 添加对下游访问失败时的自动重试机制
     * 需要启用 EnableResilientMethods
     * @return
     */
    @Retryable(maxRetries = 5, includes = IllegalStateException.class)
    @GetMapping("/cats-retry")
    CatFacts catsRetry(){
        if (this.counter.incrementAndGet() <  5){
            IO.println("oops!");
            throw new IllegalStateException("no sangria!");
        }
        IO.println("yay!");
        return client.facts();
    }

    /**
     * 添加上游最大访问并发数
     * 需要启用 EnableResilientMethods
     * @return
     */
    @ConcurrencyLimit(10)
    @GetMapping("/cats-limit")
    CatFacts catsLimit(){
        if (this.counter.incrementAndGet() <  5){
            IO.println("oops!");
            throw new IllegalStateException("no sangria!");
        }
        IO.println("yay!");
        return client.facts();
    }
}

@RestController
class DogsController{

    private final DogRepository dogRepository;

    public DogsController(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * 未添加安全密钥相关时，注释掉pom中security相关
     * @return
     */
    @GetMapping("/dogs")
    Collection<Map<String, Object>> dogs(){
        return dogRepository.findAll()
                .stream()
                .map(dog -> Map.of("fullName", (Object) dog.name(), "id", dog.id()))
                .toList();
    }

    /**
     * 指定api版本号
     * @return
     */
    @GetMapping(value = "/dogs", version = "1.1")
    Collection<Dog> dogsv11(){
        return dogRepository.findAll();
    }
}


interface DogRepository extends ListCrudRepository<Dog,Integer> {
    // 添加数据库方言后使用，验证
    // sprin data 会在编译时生成对应的查询代码
    Collection<Dog> findByName(String name);
}

record Dog(@Id int id, String name, String owner, String description) {}


/**
 * 使用自定义注解
 * 隐式注册bean，也可以使用config或者regist显式注册
 */
@Hjay
class MyBean implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        IO.println("Hello World!");
    }
}


/**
 * 创建自定义 bean 注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@interface Hjay {

    /**
     * Alias for {@link Component#value}.
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

}