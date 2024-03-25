package com.zzz.flink.flinkcdcproducer.service.impl;

import com.zzz.flink.flinkcdcproducer.service.AbstractMigrationTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2024/3/25 10:52
 * @description:
 */
@Service("MySQLTableService")
public class MySQLTableServiceImpl extends AbstractMigrationTable {

    @Override
    public int queryAllTableCount() {
        return 2;
    }
}
