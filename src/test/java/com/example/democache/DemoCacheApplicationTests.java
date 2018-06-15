package com.example.democache;

import com.example.democache.redis.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DemoCacheApplicationTests {

    /**
     * 操作对象
     */
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 操作字符串
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String,User> userRedisTemplate;
    @Test
    public void contextLoads() {
        redisTemplate.opsForValue().set("user","李四");
        User user = new User();
        user.setAge(12);
        user.setId(1);
        user.setUsername("中过");
        userRedisTemplate.opsForValue().set("user1",user);
    }
    @Test
    public void test1(){
        stringRedisTemplate.opsForValue().append("11","测试");
        redisTemplate.opsForList().leftPush("list","list1");
    }
}
