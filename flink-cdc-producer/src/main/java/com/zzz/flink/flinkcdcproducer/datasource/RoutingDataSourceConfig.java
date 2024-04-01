package com.zzz.flink.flinkcdcproducer.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2024/3/29 16:05
 * @description:
 */
//@Configuration
public class RoutingDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        // 创建并返回主数据源
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        // 创建并返回次数据源
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 设置默认数据源为主数据源
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());

        // 配置目标数据源
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("primary", primaryDataSource());
        dataSourceMap.put("secondary", secondaryDataSource());
        dynamicDataSource.setTargetDataSources(dataSourceMap);

        return dynamicDataSource;
    }
}
