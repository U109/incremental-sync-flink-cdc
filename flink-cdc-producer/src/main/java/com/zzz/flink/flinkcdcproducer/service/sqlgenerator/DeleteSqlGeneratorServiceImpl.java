package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.alibaba.fastjson2.JSONObject;
import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:17
 * @description:
 */
@Service("DELETE")
@Slf4j
public class DeleteSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        String beforeData = dataChangeInfo.getBeforeData();
        Map<String, Object> beforeDataMap = JSONObjectUtils.JsonToMap(beforeData);
        StringBuilder wherePart = new StringBuilder();
        for (String key : beforeDataMap.keySet()) {
            Object beforeValue = beforeDataMap.get(key);
            if ("create_time".equals(key)){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                beforeValue = dateFormat.format(beforeValue);
            }
            if (wherePart.length() > 0) {
                // 不是第一个更改的字段，增加逗号分隔
                wherePart.append(", ");
            }
            wherePart.append(quoteIdentifier(key)).append(" = ").append(formatValue(beforeValue));
        }
        log.info("wherePart : {}", wherePart);
        return "DELETE FROM " + dataChangeInfo.getTableName() + " WHERE " + wherePart;
    }
}
