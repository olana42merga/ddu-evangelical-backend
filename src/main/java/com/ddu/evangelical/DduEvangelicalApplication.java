package com.ddu.evangelical;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DduEvangelicalApplication {
    public static void main(String[] args) {
        SpringApplication.run(DduEvangelicalApplication.class, args);
    }
}
