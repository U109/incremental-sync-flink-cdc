package com.zzz.flink.flinkcdcproducer.service;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2024/3/25 11:22
 * @description:
 */
public interface IncrementSyncService {

    boolean generatorQueues(String exchangeName, List<MigrationTable> migrationTables);

}
