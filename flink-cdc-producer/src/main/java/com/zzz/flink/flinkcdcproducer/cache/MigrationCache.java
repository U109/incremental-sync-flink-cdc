package com.zzz.flink.flinkcdcproducer.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2024/3/25 16:37
 * @description:
 */
public class MigrationCache {

    public static final Map<String, String> MIGRATION_TABLE_CACHE = new HashMap<>();
    public static final String EXCHANGE_NAME = "mysql_exchange";
}
