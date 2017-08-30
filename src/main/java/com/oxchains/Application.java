package com.oxchains;

import com.oxchains.auth.JwtAuthentication;
import com.oxchains.bean.model.ziyun.TabUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * BaseController
 *
 * @author liuruichao
 * Created on 2017/4/6 11:25
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories("com.oxchains.dao")
public class Application {
    @RequestMapping("/")
    public String hello() {
        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static Optional<TabUser> userContext() {
        return ((JwtAuthentication) getContext().getAuthentication()).user();
    }
}
