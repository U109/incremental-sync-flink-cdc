package com.zzz.flink.flinkcdcproducer.datachange;

import lombok.Data;
import org.apache.kafka.connect.data.Struct;

/**
 * @author zhangzhongzhen wrote on 2024/3/24
 * @version 1.0
 * @description:
 */
@Data
public class DataChangeInfo {

    /**
     * 变更前数据
     */
    private Struct beforeData;
    /**
     * 变更后数据
     */
    private Struct afterData;
    /**
     * 变更类型 1新增 2修改 3删除
     */
    private String eventType;
    /**
     * binlog文件名
     */
    private String fileName;
    /**
     * binlog当前读取点位
     */
    private Integer filePos;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 变更时间
     */
    private Long changeTime;

}
