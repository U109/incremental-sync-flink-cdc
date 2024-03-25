package com.zzz.flink.flinkcdcproducer.events.mysql;

import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.datachange.DataChangeSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author zhangzhongzhen wrote on 2024/3/24
 * @version 1.0
 * @description:
 */
@Component
public class MysqlEventListener implements ApplicationRunner {

    private final DataChangeSink dataChangeSink;

    public MysqlEventListener(DataChangeSink dataChangeSink) {
        this.dataChangeSink = dataChangeSink;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DebeziumSourceFunction<DataChangeInfo> dataChangeInfoMySqlSource = buildDataChangeSourceRemote();
        DataStream<DataChangeInfo> streamSource = env
                .addSource(dataChangeInfoMySqlSource, "mysql-source")
                .setParallelism(1);
        streamSource.addSink(dataChangeSink);
        env.execute("mysql-stream-cdc");
    }

    private DebeziumSourceFunction<DataChangeInfo> buildDataChangeSourceLocal() {
        return MySqlSource.<DataChangeInfo>builder()
                .hostname("127.0.0.1")
                .port(3306)
                .username("root")
                .password("0507")
                .databaseList("flink-cdc-producer")
                .tableList("flink-cdc-producer.producer_content", "flink-cdc-producer.name_content")
                /*
                 * initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(StartupOptions.latest())
                .deserializer(new MysqlDeserialization())
                .serverTimeZone("GMT+8")
                .build();
    }

    private DebeziumSourceFunction<DataChangeInfo> buildDataChangeSourceRemote() {
        StringJoiner tableJoiner = new StringJoiner(",");
        for (int i = 1; i <= 31; i++) {
            tableJoiner.add("migration_tables.table_" + i);
        }
        return MySqlSource.<DataChangeInfo>builder()
                .hostname("117.72.32.234")
                .port(3306)
                .username("root")
                .password("Zz24270507..")
                .databaseList("migration_tables")
                .tableList(tableJoiner.toString())
                /*
                 * initial初始化快照,即全量导入后增量导入(检测更新数据写入)
                 * latest:只进行增量导入(不读取历史变化)
                 * timestamp:指定时间戳进行数据导入(大于等于指定时间错读取数据)
                 */
                .startupOptions(StartupOptions.latest())
                .deserializer(new MysqlDeserialization())
                .serverTimeZone("GMT+8")
                .build();
    }
}
