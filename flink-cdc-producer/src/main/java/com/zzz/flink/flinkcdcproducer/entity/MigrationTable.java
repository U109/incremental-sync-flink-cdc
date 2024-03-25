package com.zzz.flink.flinkcdcproducer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zzz
 * @date: 2024/3/25 10:56
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MigrationTable {

    private String tableName;

}
