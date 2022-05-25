package com.fbxmtjqj.pushserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PushServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PushServerApplication.class, args);
    }

}
