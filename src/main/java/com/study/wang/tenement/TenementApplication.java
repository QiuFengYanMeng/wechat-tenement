package com.study.wang.tenement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TenementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenementApplication.class, args);
    }

}
