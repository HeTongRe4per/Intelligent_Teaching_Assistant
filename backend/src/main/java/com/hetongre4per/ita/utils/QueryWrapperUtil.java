package com.hetongre4per.ita.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QueryWrapperUtil {
    
    public static <T> QueryWrapper<T> build(Class<T> clazz, Map<String, String[]> paramMap) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Map<String, PropertyDescriptor> propertyDescriptorMap = BeanUtil.getPropertyDescriptorMap(clazz, false);
        for (String fieldName : propertyDescriptorMap.keySet()) {
            String type = propertyDescriptorMap.get(fieldName).getPropertyType().toString();
            String columnName = StrUtil.toUnderlineCase(fieldName);
            // 处理范围查询 xxxStart~xxxEnd
            if (paramMap.containsKey(fieldName + "Start") && StrUtil.isNotBlank(paramMap.get(fieldName + "Start")[0])) {
                queryWrapper.ge(columnName, parseValueByType(paramMap.get(fieldName + "Start")[0], type));
            }
            if (paramMap.containsKey(fieldName + "End") && StrUtil.isNotBlank(paramMap.get(fieldName + "End")[0])) {
                queryWrapper.le(columnName, parseValueByType(paramMap.get(fieldName + "End")[0], type));
            }
            if (!isIgnoreField(fieldName) && paramMap.containsKey(fieldName) && propertyDescriptorMap.get(fieldName).getReadMethod() != null) {
                String fieldValue = paramMap.get(fieldName)[0];
                if (fieldValue.contains("*")) {
                    if (!fieldValue.equals("*")) {
                        if (fieldValue.startsWith("*") && fieldValue.endsWith("*")) {
                            queryWrapper.like(columnName, dealWithSpecialStr(fieldValue.substring(1, fieldValue.length() - 1)));
                        } else if (fieldValue.startsWith("*")) {
                            queryWrapper.likeLeft(columnName, dealWithSpecialStr(fieldValue.substring(1)));
                        } else if (fieldValue.endsWith("*")) {
                            queryWrapper.likeRight(columnName, dealWithSpecialStr(fieldValue.substring(0, fieldValue.length() - 1)));
                        }
                    }
                } else if (fieldValue.contains("!")) {
                    if (!fieldValue.equals("!")) {
                        queryWrapper.ne(columnName, fieldValue.substring(1));
                    }
                } else if (fieldValue.contains(",")){
                    String[] inValues = fieldValue.split(",");
                    if (inValues.length > 0) {
                        queryWrapper.in(columnName, inValues);
                    }
                } else {
                    if (StrUtil.isNotBlank(fieldValue)) {
                        queryWrapper.eq(columnName, fieldValue);
                    }
                }
            }
        }
        return queryWrapper;
    }
    
    private static Object parseValueByType(String value, String type) {
        Object finalValue = null;
        switch (type) {
            case "class java.lang.Integer":
                finalValue = Integer.parseInt(value);
                break;
            case "class java.lang.Short":
                finalValue = Short.parseShort(value);
                break;
            case "class java.lang.Long":
                finalValue = Long.parseLong(value);
                break;
            case "class java.lang.Float":
                finalValue = Float.parseFloat(value);
                break;
            case "class java.lang.Double":
                finalValue = Double.parseDouble(value);
                break;
            case "class java.utils.Date":
                finalValue = DateUtil.parse(value);
                break;
            default:
                finalValue = value;
        }
        return finalValue;
    }
    
    private static String dealWithSpecialStr(String value) {
        List<String> specialChars = Arrays.asList("_", "%");
        for (String specialChar : specialChars) {
            if (value.contains(specialChar)) {
                value = value.replaceAll(specialChar, "\\" + specialChar);
            }
        }
        return value;
    }
    
    private static boolean isIgnoreField(String fieldName) {
        if (StrUtil.equals(fieldName, "class") || StrUtil.equals(fieldName, "ids") || StrUtil.equals(fieldName, "order") || StrUtil.equals(fieldName, "sort")) {
            return true;
        }
        return false;
    }
    
}
