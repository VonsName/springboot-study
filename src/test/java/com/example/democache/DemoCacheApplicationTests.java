package com.example.democache;

import com.example.democache.redis.User;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;
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

    /**
     * RabbitAutoConfiguration
     *      RabbitProperties
     *      CachingConnectionFactory
     *      AmqpAdmin
     *      点对点模式
     */
    @Test
    public void testRabbit1(){
        /*
         * 自定义消息
         */
        String msg="测试rabbitmq";
        //消息头
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(msg.getBytes(),messageProperties);
        rabbitTemplate.send("exchange.direct","fs",message);
    }


    /**
     * 订阅模式 发送消息并自动转换，可以自定义成序列化器
     */
    @Test
    public void testRabbit2(){
        User user = new User();
        user.setAge(23);
        user.setUsername("李四");
        user.setId(2);
        user.setLastModified(DateUtils.formatDate(new Date()));
        rabbitTemplate.convertAndSend("exchange.topic","fs",user);
    }

    /**
     * 广播消息  该模式下 所有交换器绑定的队列都能接受
     */
    @Test
    public void testRabbit3(){
        User user = new User();
        user.setAge(25);
        user.setUsername("李四");
        user.setId(2);
        user.setLastModified(DateUtils.formatDate(new Date()));
        rabbitTemplate.convertAndSend("exchange.fanout","fs",user);
    }
    /**
     * 接受消息
     */
    @Test
    public void testRabbitReceive(){
        User fs = (User) rabbitTemplate.receiveAndConvert("fs.news");
        System.out.println(fs.getClass().getTypeName());
        System.out.println(fs.getUsername());
    }

    /**
     * 创建交换器
     */
    @Test
    public void createExchange(){
        //创建交换器
       // amqpAdmin.declareExchange(new DirectExchange("amqp.exchange"));
        //创建队列
        //amqpAdmin.declareQueue(new Queue("amqpadmin.queue"));
        //创建绑定规则 将消息队列绑定到指定的交换器上并指定路由key
       /* amqpAdmin.declareBinding(new Binding("amqpadmin.queue",
                                Binding.DestinationType.QUEUE,
                                "amqp.exchange","amqpadmin",null));*/
       //删除队列
       //amqpAdmin.deleteQueue("amqpadmin.queue");
        //删除交换器
        amqpAdmin.deleteExchange("amqp.exchange");
    }
}
