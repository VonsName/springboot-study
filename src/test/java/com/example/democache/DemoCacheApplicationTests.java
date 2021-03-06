package com.example.democache;

import com.example.democache.elk.Article;
import com.example.democache.elk.ArticleRepository;
import com.example.democache.redis.User;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.http.client.utils.DateUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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

    @Autowired
    private JestClient jestClient;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JavaMailSenderImpl javaMailSender;
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

    /**
     * Jest elasticsearch
     */
    @Test
    public void testElasticSearch(){
        Article article = new Article();
        article.setId(1);
        article.setAuthor("西门庆");
        article.setName("水浒传");

        //构建一个索引
        Index index = new Index
                .Builder(article)
                .index("article")
                .type("news")
                .build();
        try {
            jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试Jest搜索
     */
    @Test
    public void testJest01(){
        //language=JSON 使用json查询表达式
        String json="{\"query\":{\"match\":{\"name\":\"水浒传\"}}}";
        //构建一个搜索
        Search search = new Search
                .Builder(json)
                .addIndex("article")
                .addType("news").build();
        try {
            //返回一个SearchResult
            SearchResult result = jestClient.execute(search);
            //List<SearchResult.Hit<Article, Void>> hits = result.getHits(Article.class);
            //System.out.println(hits);
            System.out.println("-------"+result.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试集成spring-boot-starter-data-elasticsearch
     * spring data elasticsearch 	elasticsearch
     * 3.1.x 	6.2.2
     * 3.0.x 	5.5.0
     * 2.1.x 	2.4.0
     * 2.0.x 	2.2.0
     * 1.3.x 	1.5.2
     * 这里使用的spring data elasticsearch 是2.4.6,所以服务端的版本要是2.x.x版本
     *
     * https://docs.spring.io/spring-data/elasticsearch/docs/3.0.6.RELEASE/reference/html/
     */
    @Test
    public void  testEs02(){
        /*Article article = new Article();
        article.setId(1);
        article.setName("西游记");
        article.setAuthor("吴承恩");
        articleRepository.index(article);*/
        List<Article> articleList = articleRepository.findArticleByName("西");
        System.out.println(articleList+"-------------");
        /*new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withFilter();*/
    }

    /**
     * 测试邮件发送简单消息
     */
    @Test
    public void testMailSend(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("开会啦");
        simpleMailMessage.setText("测试开会发邮件");
        simpleMailMessage.setTo("vons0828@163.com");
        simpleMailMessage.setFrom("894611653@qq.com");
        javaMailSender.send(simpleMailMessage);
    }

    /**
     *  复杂消息
     * @throws MessagingException
     */
    @Test
    public void testMailSend2() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //multipart=true 上传文件
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setSubject("复杂消息+上传附件");
        //html=true 表示发送Html消息
        messageHelper.setText("<a href='www.baidu.com'>点击激活帐号</a>",true);
        messageHelper.setTo("vons0828@163.com");
        messageHelper.setFrom("894611653@qq.com");
        messageHelper.addAttachment("1.png",new File("C:\\Users\\ASUS\\Pictures\\Saved Pictures\\1.png"));
        messageHelper.addAttachment("2.png",new File("C:\\Users\\ASUS\\Pictures\\Saved Pictures\\2.png"));
        javaMailSender.send(mimeMessage);
    }
}
