package com.zzz.flink.flinkcdcconsumer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2024/3/26 10:45
 * @description:
 */
public class MessageUtils {
    // 使用静态的 ObjectMapper 以提高效率，避免重复创建对象消耗资源。
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static List<String> disposeMessage(String msg) {
        try {
            // 解析 JSON 字符串到 Map
            Map map = OBJECT_MAPPER.readValue(msg, Map.class);
            // 强制转换 'all_queue' 键对应的值为 List<String>
            return (List<String>) map.get("all_queue");
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
