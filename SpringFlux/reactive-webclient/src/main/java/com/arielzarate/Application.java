package com.arielzarate;

import com.arielzarate.service.LoginReactiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(LoginReactiveService loginService) {
        return args -> {
            log.info("========================================");
            log.info("Starting REACTIVE FakeStoreAPI Login Test");
            log.info("========================================");

            loginService.loginReactive()
                    .doOnSuccess(token -> {
                        log.info("========================================");
                        log.info("LOGIN SUCCESSFUL!");
                        log.info("Token: {}", token);
                        log.info("========================================");
                    })
                    .doOnError(error -> {
                        log.error("LOGIN FAILED: {}", error.getMessage());
                    })
                    .subscribe();
        };
    }
}
