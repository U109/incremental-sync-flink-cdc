package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:16
 * @description:
 */
public interface SqlGeneratorService {

    String generatorSql(DataChangeInfo dataChangeInfo);
}
