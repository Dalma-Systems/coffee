package com.dalma.coffee.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.dalma.coffee", "com.bybright.logging"})
public class DalmaCoffeeApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DalmaCoffeeApiApplication.class, args);
    }
}
