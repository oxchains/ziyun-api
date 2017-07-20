package com.oxchains;

import com.oxchains.common.FabricAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.oxchains.util.ResponseUtil.*;

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
@Slf4j
public class Application {

    @Value("${fabric.username}") private String username;
    @Value("${fabric.password}") private String password;
    @Value("${fabric.affiliation}") private String affiliation;
    @Value("${fabric.uri}") private String uri;

    @RequestMapping("/")
    public String hello() {
        return "hello";
    }

    @Bean({ "token" })
    String token() {
        String resp = new RestTemplate().postForObject(uri + "/user/token", new FabricAccount(username, password, affiliation), String.class);
        Optional<String> tokenOptional = extract(resp, "/data/token");
        if (tokenOptional.isPresent()) {
            String token = "Bearer " + tokenOptional.get();
            log.info("access token for fabric manager: {}", token);
            return token;
        } else throw new IllegalStateException("system failed to init: cannot get token from fabric manager!");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
