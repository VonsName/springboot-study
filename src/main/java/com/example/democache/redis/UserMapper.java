package com.example.democache.redis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ASUS
 */
@Mapper
public interface UserMapper {

    @Select(value = "select * from user where id=#{id}")
    public User selectById(Integer id);
    @Update(value = "update user set username=#{username} where id=#{id}")
    public void update(User user);
    @Delete("delete from user where id=#{id}")
    public void delete(Integer id);
}
