package com.jenfer;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.jenfer.mappers")
@SpringBootApplication
public class CommonApplication {


    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);

    }


}
