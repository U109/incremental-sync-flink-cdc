package com.zzz.flink.flinkcdcconsumer.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Zzz
 * @date: 2024/3/26 10:41
 * @description:
 */
@Configuration
public class FlinkQueueListenerConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    private ThreadPoolTaskExecutor threadPool;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(10); // 核心线程数
        threadPool.setMaxPoolSize(30); // 最大线程数
        threadPool.setQueueCapacity(500); // 队列容量
        threadPool.setThreadNamePrefix("rabbitmq-listener-");
        threadPool.initialize();
    }

    public SimpleMessageListenerContainer createMessageListenerContainer(String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(new MessageListenerAdapter(new FlinkMessageListener(jdbcTemplate))); // Check if FlinkMessageListener can be a singleton
        return container;
    }

    public void registerListener(String queueName) {
        this.threadPool.submit(() -> {
            SimpleMessageListenerContainer container = createMessageListenerContainer(queueName);
            container.start();
        });
    }

    public void registerListener(List<String> queueList) {
        queueList.forEach(queueName -> {
            this.threadPool.submit(() -> {
                SimpleMessageListenerContainer container = createMessageListenerContainer(queueName);
                container.start();
            });
        });
    }
}
