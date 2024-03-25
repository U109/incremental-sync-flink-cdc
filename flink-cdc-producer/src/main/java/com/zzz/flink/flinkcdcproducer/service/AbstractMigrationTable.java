package com.zzz.flink.flinkcdcproducer.service;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author: Zzz
 * @date: 2024/3/25 11:13
 * @description:
 */
public abstract class AbstractMigrationTable implements MigrationTableService {
    @Override
    public List<MigrationTable> queryAllTable() {

        return IntStream.rangeClosed(1, 31) // 使用rangeClosed方法包括上限32
                .mapToObj(i -> new MigrationTable("table_" + i)) // 将整数转换为MigrationTable对象
                .collect(Collectors.toCollection(ArrayList::new)); // 收集到ArrayList
    }

    @Override
    public int queryAllTableCount() {
        return 0;
    }

}
