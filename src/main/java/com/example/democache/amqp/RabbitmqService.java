package com.example.democache.amqp;

import com.example.democache.redis.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author ASUS
 */
@Service
public class RabbitmqService {

    /**
     * 监听消息
     * @param user 用户
     */
    @RabbitListener(queues = "fs")
    public void receive(User user){
        System.out.println("收到消息"+user);
    }

    /**
     * 监听多个队列的消息
     * @param message 消息
     */
    @RabbitListener(queues = {"fs.emps","fs.news","vons.news"})
    public void receive2(Message message){
        byte[] body = message.getBody();
        System.out.println(new String(body));
        System.out.println(message.getMessageProperties());
    }
}
