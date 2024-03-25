package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:17
 * @description:
 */
@Service("DELETE")
public class DeleteSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        Struct beforeData = dataChangeInfo.getBeforeData();
        Schema beforeSchema = beforeData.schema();

        List<Field> beforeFields = beforeSchema.fields();

        StringBuilder wherePart = new StringBuilder();
        for (Field field : beforeFields) {
            Object beforeValue = beforeData.get(field);
            if (wherePart.length() > 0) {
                // 不是第一个更改的字段，增加逗号分隔
                wherePart.append(", ");
            }
            wherePart.append(quoteIdentifier(field.name())).append(" = ").append(formatValue(beforeValue));
        }
        return "DELETE FROM " + dataChangeInfo.getTableName() + " WHERE " + wherePart;
    }
}
