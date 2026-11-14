package com.hjay.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * security 配置类
 */
@EnableWebSecurity
@Configuration
public class AppConfigSecurity {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests.requestMatchers("/", "/index.html", "/login.html", "/error",
                            "/*.png", "/*.jpg", "/a/**").permitAll()
                    .anyRequest().authenticated();
        });

        http.formLogin(formLogin -> {
            formLogin.loginPage("/login.html").permitAll();
        });

        return http.build();
    }


    /**
     * 保存用户缓存
     * @return
     */
    @Bean
    UserDetailsService userDetailsService() {

        // 模拟从数据库获取用户及其权限
        UserDetails zhangsan = User.withUsername("zhangsan").password("123")
                .roles("admin", "user").authorities("file_read", "file_write")
                .build();
        UserDetails lisi = User.withUsername("lisi").password("123")
                .roles("hr", "user").authorities("file_read")
                .build();

        // 使用内存保存所有的用户信息
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager(zhangsan, lisi);

        return detailsManager;
    }

    /**
     * 需要配置密码加密器
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
