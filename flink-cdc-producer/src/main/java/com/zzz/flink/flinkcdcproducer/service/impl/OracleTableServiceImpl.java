package com.zzz.flink.flinkcdcproducer.service.impl;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;
import com.zzz.flink.flinkcdcproducer.service.AbstractMigrationTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Zz
 * @date: 2024/3/25 13:50
 * @description:
 */
@Service("OracleTableService")
public class OracleTableServiceImpl extends AbstractMigrationTable {

    @Override
    public int queryAllTableCount() {
        return 1;
    }
}
