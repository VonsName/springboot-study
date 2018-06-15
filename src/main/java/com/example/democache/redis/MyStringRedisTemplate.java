package com.example.democache.redis;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author ASUS
 * 自定义redis序列化
 * 引入了redis的jar包，缓存RedisCacheManager 之后，默认是使用redis作缓存管理了
 */
@Configuration
public class MyStringRedisTemplate {

    @Bean
    public RedisTemplate<String,User> userRedisTemplate(
            RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, User> objectObjectRedisTemplate = new RedisTemplate<>();
        objectObjectRedisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<User> userJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(User.class);
        objectObjectRedisTemplate.setDefaultSerializer(userJackson2JsonRedisSerializer);
        return objectObjectRedisTemplate;
    }

    /**
     * 自定义缓存管理器 设置自定义RedisTemplate
     * @param userRedisTemplate user
     *                          Primary指定默认的管理器
     * @return RedisCacheManager
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, User> userRedisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(userRedisTemplate);
        //cacheManager.setUsePrefix(true); 会使cache的name作为前缀
        cacheManager.setUsePrefix(false);
        return cacheManager;
    }
}
