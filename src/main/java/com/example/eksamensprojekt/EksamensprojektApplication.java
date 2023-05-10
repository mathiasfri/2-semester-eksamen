package com.example.eksamensprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.eksamensprojekt.repository")
public class EksamensprojektApplication {
    public static void main(String[] args) {
        SpringApplication.run(EksamensprojektApplication.class, args);
    }
}
