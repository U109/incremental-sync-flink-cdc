package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:17
 * @description:
 */
@Service("CREATE")
@Slf4j
public class InsertSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        String afterData = dataChangeInfo.getAfterData();
        Map<String, Object> afterDataMap = JSONObjectUtils.JsonToMap(afterData);
        StringJoiner columnSetPart = new StringJoiner(",");
        StringJoiner valuePart = new StringJoiner(",");

        for (String key : afterDataMap.keySet()) {
            Object afterValue = afterDataMap.get(key);
            columnSetPart.add(quoteIdentifier(key));
            valuePart.add(formatValue(afterValue));
        }
        log.info("columnSetPart : {}", columnSetPart);
        log.info("valuePart : {}", valuePart);
        return "INSERT INTO " + dataChangeInfo.getTableName() + "(" + columnSetPart + ") VALUES("
                + valuePart + ");";
    }
}
