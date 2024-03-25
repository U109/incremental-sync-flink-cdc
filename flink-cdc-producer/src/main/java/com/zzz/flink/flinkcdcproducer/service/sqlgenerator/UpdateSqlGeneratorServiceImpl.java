package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:17
 * @description:
 */
@Service("UPDATE")
public class UpdateSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        Struct beforeData = dataChangeInfo.getBeforeData();
        Struct afterData = dataChangeInfo.getAfterData();

        Schema beforeSchema = beforeData.schema();
        List<Field> beforeFields = beforeSchema.fields();

        StringBuilder updateSetPart = new StringBuilder();
        StringBuilder wherePart = new StringBuilder();

        for (Field field : beforeFields) {
            Object beforeValue = beforeData.get(field);
            Object afterValue = afterData.get(field);
            if (!beforeValue.equals(afterValue)) {
                // 如果字段值发生变化，则将其加入到更新列表
                if (updateSetPart.length() > 0) {
                    // 不是第一个更改的字段，增加逗号分隔
                    updateSetPart.append(", ");
                }
                updateSetPart.append(quoteIdentifier(field.name()))
                        .append(" = ")
                        .append(formatValue(afterValue));
            } else {
                if (wherePart.length() > 0) {
                    // 不是第一个更改的字段，增加逗号分隔
                    wherePart.append(", ");
                }
                wherePart.append(quoteIdentifier(field.name()))
                        .append(" = ")
                        .append(formatValue(beforeValue));
            }
        }
        // 构建完整 SQL
        return "UPDATE " + quoteIdentifier(dataChangeInfo.getTableName())
                + " SET " + updateSetPart
                + " WHERE " + wherePart + ";";
    }
}
