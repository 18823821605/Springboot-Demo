package com.springboot.dlc.config;


import com.alibaba.fastjson.JSON;
import com.springboot.dlc.common.redis.ReceiveMessage;
import com.springboot.dlc.common.result.SysConstant;
import com.springboot.dlc.modules.entity.SysManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: liujiebang
 * @Description: redis配置类
 * @Date: 2018/6/27 15:05
 **/
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {


    public final static String channle = "springBoot-message";
    public final static String channle2 = "springBoot-message2";


    @Bean
    public RedisTemplate<Serializable, Serializable> redisTemplate(
            JedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        redisTemplate.opsForValue().get(SysConstant.PROJECT_NAME);
        log.info("-------------------------------------------------");
        log.info("---------------local redis success---------------");
        log.info("-------------------------------------------------");
        return redisTemplate;
    }

    /**
     * 配置监听器1
     *
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(ReceiveMessage receiveMessage) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiveMessage, "receiveMessage");
        messageListenerAdapter.setSerializer(new JdkSerializationRedisSerializer());
        return messageListenerAdapter;
    }


    /**
     * 配置监听器2
     *
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter2(ReceiveMessage receiveMessage) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiveMessage, "receiveMessage2");
        messageListenerAdapter.setSerializer(new JdkSerializationRedisSerializer());
        return messageListenerAdapter;
    }

    /**
     * 初始化监听器
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @param listenerAdapter2
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(JedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter,
                                                   MessageListenerAdapter listenerAdapter2) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(channle));
        container.addMessageListener(listenerAdapter2, new PatternTopic(channle2));
        return container;
    }

    /**
     * 消息监听者1
     *
     * @return
     */
    @Bean
    public ReceiveMessage receiveMessage(CountDownLatch countDownLatch) {
        return new ReceiveMessage(countDownLatch);
    }

    /**
     * 消息监听者2
     *
     * @return
     */
    @Bean
    public ReceiveMessage receiveMessage2(CountDownLatch countDownLatch) {
        return new ReceiveMessage(countDownLatch);
    }

    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(1);
    }


}

