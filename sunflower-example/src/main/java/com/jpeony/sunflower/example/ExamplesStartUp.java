package com.jpeony.sunflower.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yihonglei
 */
@SpringBootApplication(scanBasePackages = "com.jpeony.*")
public class ExamplesStartUp implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ExamplesStartUp.class, args);
    }
}
