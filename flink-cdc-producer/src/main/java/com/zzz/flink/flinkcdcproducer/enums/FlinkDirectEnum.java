package com.zzz.flink.flinkcdcproducer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhangzhongzhen wrote on 2024/3/12
 * @version 1.0
 * @description: 生产者和消费者通信队列
 */
@Getter
@RequiredArgsConstructor
public enum FlinkDirectEnum {
    FLINK_CDC_EXCHANGE("flink_cdc_exchange"),
    FLINK_CDC_QUEUE("flink_cdc_exchange_consumer.queue"),
    FLINK_CDC_ROUTING_KEY("flink_cdc_exchange_consumer.key");
    private final String name;

}
