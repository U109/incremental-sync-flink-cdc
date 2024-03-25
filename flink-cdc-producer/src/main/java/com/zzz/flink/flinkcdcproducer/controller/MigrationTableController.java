package com.zzz.flink.flinkcdcproducer.controller;

import com.zzz.flink.flinkcdcproducer.entity.MigrationTable;
import com.zzz.flink.flinkcdcproducer.service.IncrementSyncService;
import com.zzz.flink.flinkcdcproducer.service.MigrationTableService;
import com.zzz.flink.flinkcdcproducer.service.impl.MySQLTableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zzz.flink.flinkcdcproducer.cache.MigrationCache.EXCHANGE_NAME;

/**
 * @author: Zzz
 * @date: 2024/3/25 10:45
 * @description:
 */
@RestController
@RequestMapping("/table")
public class MigrationTableController {

    @Autowired
    private Map<String, MigrationTableService> migrationTableServiceMap;

    @Resource
    private IncrementSyncService incrementSyncService;

    @GetMapping("/queryAllTable/{typeName}")
    public void queryAllTable(@PathVariable String typeName) {
        MigrationTableService migrationTableService = migrationTableServiceMap.get(typeName);
        System.out.println(migrationTableService.queryAllTableCount());
    }

    @GetMapping("/getAllMigrationType")
    public void getAllMigrationType() {
        migrationTableServiceMap.forEach((typeName, type) -> {
            System.out.println("typeName : " + typeName);
            System.out.println("type : " + type);
        });
    }

    @GetMapping("/migration/{typeName}")
    public void migration(@PathVariable String typeName) {
        MigrationTableService migrationTableService = migrationTableServiceMap.get(typeName);
        //查询所有表，已经创建好的31张表
        List<MigrationTable> allTableList = migrationTableService.queryAllTable();
        //将要增量迁移的表封装进队列Map
        incrementSyncService.generatorQueues(EXCHANGE_NAME, allTableList);
    }
}
