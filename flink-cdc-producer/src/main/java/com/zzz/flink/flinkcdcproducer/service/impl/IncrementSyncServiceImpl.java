package com.zzz.flink.flinkcdcproducer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;
import com.zzz.flink.flinkcdcproducer.enums.FlinkDirectEnum;
import com.zzz.flink.flinkcdcproducer.service.IncrementSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.EXCHANGE_NAME;
import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.MIGRATION_TABLE_CACHE;

/**
 * @author: Zzz
 * @date: 2024/3/25 11:22
 * @description:
 */
@Service("incrementSyncService")
@Slf4j
public class IncrementSyncServiceImpl implements IncrementSyncService {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ConfirmService confirmService;

    final String QUEUE_PREFIX = "key_"; // 移至常量，便于维护
    final String QUEUE_SUFFIX = "_queue"; // 移至常量，便于维护
    final int BATCH_SIZE = 20; // 移至常量，便于调整

    @Override
    public void generatorQueues(String exchangeName, List<MigrationTable> migrationTables) {
        if (migrationTables == null || migrationTables.isEmpty()) {
            log.error("Migration tables are empty or null. Nothing to process.");
            return;
        }
        TopicExchange topicExchange = new TopicExchange(exchangeName, true, false);

        int batchCount = (int) Math.ceil((double) migrationTables.size() / BATCH_SIZE);

        Map<String, Object> message = new HashMap<>();
        message.put("exchange", exchangeName);
        message.put("all_queue", new ArrayList<>());
        // 优化性能：尝试预先声明所有需要的队列和交换器
        try {
            for (int i = 1; i <= batchCount; i++) {
                Queue queue = new Queue(QUEUE_PREFIX + i + QUEUE_SUFFIX, true);
                ((ArrayList) message.get("all_queue")).add(QUEUE_PREFIX + i + QUEUE_SUFFIX);
                amqpAdmin.declareQueue(queue);
                if (i == 1) { // 仅在第一次迭代时声明交换器，避免重复声明
                    amqpAdmin.declareExchange(topicExchange);
                }
                amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(QUEUE_PREFIX + i));
            }
            for (int i = 0; i < migrationTables.size(); i++) {
                MIGRATION_TABLE_CACHE.put(migrationTables.get(i).getTableName(), QUEUE_PREFIX + (i + BATCH_SIZE - 1) / BATCH_SIZE);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            rabbitTemplate.setReturnsCallback(confirmService);
            rabbitTemplate.setConfirmCallback(confirmService);
            rabbitTemplate.convertAndSend(FlinkDirectEnum.FLINK_CDC_EXCHANGE.getName(),
                    FlinkDirectEnum.FLINK_CDC_ROUTING_KEY.getName(), objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            log.error("Error while declaring queues, exchanges, or bindings. " + e.getMessage());
        }
    }
}
