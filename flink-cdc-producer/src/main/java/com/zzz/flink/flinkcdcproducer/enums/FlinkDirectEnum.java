package com.zzz.flink.flinkcdcproducer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhangzhongzhen wrote on 2024/3/12
 * @version 1.0
 * @description:
 */
@Getter
@RequiredArgsConstructor
public enum FlinkDirectEnum {

    ORDER_EXCHANGE("flink_cdc_exchange"),
    ORDER_QUEUE("localOrder.queue"),
    ORDER_ROUTING_KEY("localOrder.key");

    private final String name;

}
