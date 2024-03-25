package com.zzz.flink.flinkcdcproducer.service.impl;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;
import com.zzz.flink.flinkcdcproducer.service.TableDataConvertService;
import com.zzz.flink.flinkcdcproducer.service.sqlgenerator.SqlGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2024/3/25 16:48
 * @description:
 */
@Service
public class TableDataConvertServiceImpl implements TableDataConvertService {

    @Autowired
    Map<String, SqlGeneratorService> sqlGeneratorServiceMap;

    @Override
    public String convertSqlByDataChangeInfo(DataChangeInfo dataChangeInfo) {
        SqlGeneratorService sqlGeneratorService = sqlGeneratorServiceMap.get(dataChangeInfo.getEventType());
        return sqlGeneratorService.generatorSql(dataChangeInfo);
    }
}
