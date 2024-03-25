package com.zzz.flink.flinkcdcproducer.service;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2024/3/25 10:52
 * @description:
 */
public interface MigrationTableService {

    List<MigrationTable> queryAllTable();

    int queryAllTableCount();
}
