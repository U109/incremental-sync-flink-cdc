package com.zzz.flink.flinkcdcproducer.util;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangzhongzhen wrote on 2024/3/25
 * @version 1.0
 * @description:
 */
public class JSONObjectUtils {

    public static Map<String, Object> JsonToMap(String jsonObj) {
        if (jsonObj == null || jsonObj.isEmpty()) {
            throw new IllegalArgumentException("输入的JSON字符串不能为空");
        }
        Map<String, Object> map = new HashMap<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonObj);
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key); // 注意：这里假设了T可以被强制转换为JSON对象的值类型
                map.put(key, value);
            }
        } catch (JSONException e) {
            // 日志记录或其他异常处理方法
            throw new IllegalArgumentException("输入的字符串不是有效的JSON格式", e);
        }
        return map;
    }
}
