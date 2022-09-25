package com.kpi.consolehello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Order(2)
public class ConsoleHelloApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.out.println("Start main");
        SpringApplication.run(ConsoleHelloApplication.class, args);
        System.out.println("End main");
    }

    @Override
    public void run(String... args) {
        System.out.println("Hello from our team!!!");
    }
}
