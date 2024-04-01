package com.zzz.flink.flinkcdcconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author: Zzz
 * @date: 2024/3/26 10:43
 * @description:
 */
@Component
@Scope("prototype")
@Slf4j
public class FlinkMessageListener implements MessageListener {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FlinkMessageListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Message message) {
        String payload = new String(message.getBody());
        // 处理消息
        log.info("Received message: {}", payload);
        jdbcTemplate.execute(payload);
        log.info("success insert");
    }

}