package com.example.democache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ASUS
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 缓存方法的返回结果
     * 使用参数的值作为key
     */
    @Cacheable(cacheNames = "name",key = "#id",condition = "#id>0",
            unless = "#result==null")
    @GetMapping(value = "/select/{id}")
    public User test(@PathVariable(value = "id") Integer id){
        return userService.selectById(id);
    }
    /**
     * 修改数据同时更新缓存
     */
    @CachePut(cacheNames = "name",key = "#user.id")
    @GetMapping(value = "update")
    public User update(User user){
        return userService.update(user);
    }

    /**
     * 删除后清空缓存
     */
    @CacheEvict(cacheNames = "name",beforeInvocation = false)
    @GetMapping(value = "/delete/{id}")
    public void delete(@PathVariable(value = "id")Integer id){
        System.out.println("删除情况缓存");
        userService.delete(id);
    }

    /**
     * Caching 组合注解
     */
    @GetMapping(value = "/cache")
    @Caching(cacheable = {@Cacheable(value = "name")},
            put = {@CachePut(value = "name")},
            evict = {@CacheEvict(cacheNames = "name")})
    public void test(){

    }
}
