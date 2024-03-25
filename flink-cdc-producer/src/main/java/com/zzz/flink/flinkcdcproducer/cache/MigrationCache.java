package com.zzz.flink.flinkcdcproducer.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Zzz
 * @date: 2024/3/25 16:37
 * @description:
 */
public class MigrationCache implements Serializable {

    public static final Map<String, String> MIGRATION_TABLE_CACHE = new ConcurrentHashMap<>();
    public static final String EXCHANGE_NAME = "mysql_exchange";
}
