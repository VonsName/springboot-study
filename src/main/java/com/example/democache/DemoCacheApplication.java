package com.example.democache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ASUS
 * springboot 默认支持两种elasticsearch
 *      1.Jest(); 默认不生效 需要导入io.searchbox.client.JestClient
 *      2.SpringData ElasticSearch
 */
@SpringBootApplication
@MapperScan(value = "com.example.democache.mapper")
@EnableCaching//自动配置缓存
//EnableRabbit+@RabbitListener 监听消息
@EnableRabbit //自动配置Rabbit

public class DemoCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCacheApplication.class, args);
    }
}
