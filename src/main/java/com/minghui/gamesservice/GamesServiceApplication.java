package com.minghui.gamesservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(scanBasePackages = "com.minghui")
@MapperScan("com.minghui.dao")
@Transactional
@EnableScheduling
@EnableAsync
public class GamesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamesServiceApplication.class, args);
    }

}
