package com.jenfer;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.jenfer"})
@MapperScan("com.jenfer.mappers")
@EnableTransactionManagement()
public class  Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
