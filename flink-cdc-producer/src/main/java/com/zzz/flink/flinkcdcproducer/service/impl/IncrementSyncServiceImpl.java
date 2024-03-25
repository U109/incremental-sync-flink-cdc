package com.zzz.flink.flinkcdcproducer.service.impl;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;
import com.zzz.flink.flinkcdcproducer.service.IncrementSyncService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.MIGRATION_TABLE_CACHE;

/**
 * @author: Zzz
 * @date: 2024/3/25 11:22
 * @description:
 */
@Service("incrementSyncService")
public class IncrementSyncServiceImpl implements IncrementSyncService {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public boolean generatorQueues(String exchangeName, List<MigrationTable> migrationTables) {
        TopicExchange topicExchange = new TopicExchange(exchangeName, true, false);
        //生成队列
        for (int i = 1; i <= migrationTables.size() / 20; i++) {
            Queue queue = new Queue("key_" + i + "_queue", true);
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareExchange(topicExchange);
            amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with("key_" + i));
        }
        for (int i = 0; i < migrationTables.size(); i++) {
            MIGRATION_TABLE_CACHE.put(migrationTables.get(i).getTableName(), "key_" + i / 20);
        }
        return true;
    }
}
