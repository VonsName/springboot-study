package com.example.democache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author ASUS
 */
@Service                        //指定缓存管理器
@CacheConfig(cacheNames = "user",cacheManager = "cacheManager")
public class UserService {

    @Autowired
    private UserMapper userMapper;

  /*  *//**
     * 操作对象
     *//*
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    *//**
     * 操作字符串
     *//*
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
*/
    public User selectById(Integer id){
        return userMapper.selectById(id);
    }
    public User update(User user){
      userMapper.update(user);
      return user;
    }
    public void delete(Integer id){
        userMapper.delete(id);
    }
}
