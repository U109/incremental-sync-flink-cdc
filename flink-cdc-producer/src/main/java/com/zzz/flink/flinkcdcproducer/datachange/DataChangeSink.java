package com.zzz.flink.flinkcdcproducer.datachange;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.service.TableDataConvertService;
import com.zzz.flink.flinkcdcproducer.service.impl.ConfirmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.EXCHANGE_NAME;
import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.MIGRATION_TABLE_CACHE;

/**
 * @author zhangzhongzhen wrote on 2024/3/24
 * @version 1.0
 * @description:
 */
@Component
@Slf4j
public class DataChangeSink implements SinkFunction<DataChangeInfo> {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    ConfirmService confirmService;

    @Resource
    TableDataConvertService tableDataConvertService;

    @Override
    public void invoke(DataChangeInfo value, Context context) {
        log.info("收到变更原始数据:{}", value);

        // todo 数据处理;因为此sink也是交由了spring管理，您想进行任何操作都非常简单
        //转换后发送到对应的MQ
        if (MIGRATION_TABLE_CACHE.containsKey(value.getTableName())) {
            String routingKey = MIGRATION_TABLE_CACHE.get(value.getTableName());
            rabbitTemplate.setReturnsCallback(confirmService);
            rabbitTemplate.setConfirmCallback(confirmService);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, tableDataConvertService.convertSqlByDataChangeInfo(value));
        }
    }
}
