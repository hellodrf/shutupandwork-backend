package com.cervidae.shutupandwork;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan
@SpringBootApplication
public class SUAWApplication {

    public static void main(String[] args) {
        SpringApplication.run(SUAWApplication.class, args);
    }

}
