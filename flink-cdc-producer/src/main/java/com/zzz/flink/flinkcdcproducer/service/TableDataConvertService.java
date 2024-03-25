package com.zzz.flink.flinkcdcproducer.service;

import com.zzz.flink.flinkcdcproducer.datachange.DataChangeInfo;

/**
 * @author: Zzz
 * @date: 2024/3/25 16:48
 * @description:
 */
public interface TableDataConvertService {

    String convertSqlByDataChangeInfo(DataChangeInfo dataChangeInfo);


}
