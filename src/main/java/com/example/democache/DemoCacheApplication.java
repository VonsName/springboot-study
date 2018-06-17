package com.example.democache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ASUS
 * springboot 默认支持两种elasticsearch
 *      1.Jest(); 默认不生效 需要导入io.searchbox.client.JestClient
 *      2.SpringData ElasticSearch
 */
@EnableAsync //开启异步自动配置
@SpringBootApplication
@MapperScan(value = "com.example.democache.mapper")
//@EnableCaching//自动配置缓存
//EnableRabbit+@RabbitListener 监听消息
@EnableRabbit //自动配置Rabbit
//定时任务注解
@EnableScheduling
public class DemoCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCacheApplication.class, args);
    }
}
