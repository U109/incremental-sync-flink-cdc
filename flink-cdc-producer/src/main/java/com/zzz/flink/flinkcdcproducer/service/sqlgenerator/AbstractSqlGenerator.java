package com.zzz.flink.flinkcdcproducer.service.sqlgenerator;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import org.apache.flink.types.Row;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Field;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2024/3/25 17:21
 * @description:
 */
public abstract class AbstractSqlGenerator implements SqlGeneratorService {
    @Override
    public String generatorSql(DataChangeInfo dataChangeInfo) {
        return null;
    }

    public String getColumnName(int columnIndex) {
        // 根据列索引获取列名的逻辑
        // 这里简化处理，实际可能需要根据场景获取真实的列名
        return "column" + columnIndex;
    }

    public String quoteIdentifier(String identifier) {
        // 对字段名进行转义处理，这里简化为对其加反引号
        // 实际应该处理数据库标识符的特殊字符
        return "`" + identifier + "`";
    }

    public String formatValue(Object value) {
        // 根据值的类型返回 SQL 语句中的字符串
        // 这里需要根据实际类型来转换
        if (value == null) {
            return "NULL";
        } else if (value instanceof String) {
            return "'" + ((String) value).replace("'", "''") + "'";
        } else if (value instanceof Number) {
            return value.toString();
        } else {
            return value.toString();
        }
    }
}
