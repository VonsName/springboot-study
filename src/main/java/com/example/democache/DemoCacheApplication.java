package com.example.democache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ASUS
 */
@SpringBootApplication
@MapperScan(value = "com.example.democache.mapper")
@EnableCaching
//EnableRabbit+@RabbitListener 监听消息
@EnableRabbit
public class DemoCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCacheApplication.class, args);
    }
}
