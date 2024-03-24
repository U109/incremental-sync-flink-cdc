package com.zzz.flink.flinkcdcproducer.mysqlevent;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;

/**
 * @author zhangzhongzhen wrote on 2024/3/24
 * @version 1.0
 * @description:
 */
@Component
@Slf4j
public class DataChangeSink implements SinkFunction<DataChangeInfo> {

    @Override
    public void invoke(DataChangeInfo value, Context context) {
        log.info("收到变更原始数据:{}", value);
        // todo 数据处理;因为此sink也是交由了spring管理，您想进行任何操作都非常简单

    }
}
