package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.util.JSONObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:17
 * @description:
 */
@Service("UPDATE")
@Slf4j
public class UpdateSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        String beforeData = dataChangeInfo.getBeforeData();
        String afterData = dataChangeInfo.getAfterData();
        Map<String, Object> beforeDataMap = JSONObjectUtils.JsonToMap(beforeData);
        Map<String, Object> afterDataMap = JSONObjectUtils.JsonToMap(afterData);

        StringBuilder updateSetPart = new StringBuilder();
        StringBuilder wherePart = new StringBuilder();
        for (String key : beforeDataMap.keySet()) {
            Object beforeValue = beforeDataMap.get(key);
            Object afterValue = afterDataMap.get(key);

            if ("create_time".equals(key)){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                beforeValue = dateFormat.format(beforeValue);
            }
            if ("create_time".equals(key)){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                afterValue = dateFormat.format(afterValue);
            }
            if (!beforeValue.equals(afterValue)) {
                // 如果字段值发生变化，则将其加入到更新列表
                if (updateSetPart.length() > 0) {
                    // 不是第一个更改的字段，增加逗号分隔
                    updateSetPart.append(", ");
                }
                updateSetPart.append(quoteIdentifier(key))
                        .append(" = ")
                        .append(formatValue(afterValue));
            } else {
                if (wherePart.length() > 0) {
                    // 不是第一个更改的字段，增加逗号分隔
                    wherePart.append(", ");
                }
                wherePart.append(quoteIdentifier(key))
                        .append(" = ")
                        .append(formatValue(beforeValue));
            }
        }
        log.info("updateSetPart : {}", updateSetPart);
        log.info("wherePart : {}", wherePart);
        // 构建完整 SQL
        return "UPDATE " + quoteIdentifier(dataChangeInfo.getTableName())
                + " SET " + updateSetPart
                + " WHERE " + wherePart + ";";
    }
}
