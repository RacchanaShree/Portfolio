package com.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        System.out.println("Starting Portfolio Spring Boot App...");
        SpringApplication.run(PortfolioApplication.class, args);
    }
}
