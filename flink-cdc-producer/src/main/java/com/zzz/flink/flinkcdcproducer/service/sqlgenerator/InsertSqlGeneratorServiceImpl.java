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
@Service("INSERT")
public class InsertSqlGeneratorServiceImpl extends AbstractSqlGenerator {

    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        Struct afterData = dataChangeInfo.getAfterData();
        Schema afterSchema = afterData.schema();
        List<Field> afterFields = afterSchema.fields();

        StringJoiner columnSetPart = new StringJoiner(",");
        StringJoiner valuePart = new StringJoiner(",");

        for (Field field : afterFields) {
            Object afterValue = afterData.get(field);
            columnSetPart.add(quoteIdentifier(field.name()));
            valuePart.add(formatValue(afterValue));
        }
        return "INSERT INTO " + dataChangeInfo.getTableName() + "(" + columnSetPart + ") VALUES("
                + valuePart + ");";
    }
}
