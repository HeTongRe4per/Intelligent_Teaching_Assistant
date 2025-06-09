package com.hetongre4per.ita.config.mybatis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JSONObjectTypeHandler extends BaseTypeHandler<JSONObject> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        // 将 JSONObject 转换为字符串并设置到 PreparedStatement 中
        String jsonString = JSON.toJSONString(parameter);
        ps.setString(i, jsonString);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从 ResultSet 中获取字符串并转换为 JSONObject
        String jsonString = rs.getString(columnName);
        return jsonString != null ? JSON.parseObject(jsonString) : null;
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从 ResultSet 中获取字符串并转换为 JSONObject
        String jsonString = rs.getString(columnIndex);
        return jsonString != null ? JSON.parseObject(jsonString) : null;
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从 CallableStatement 中获取字符串并转换为 JSONObject
        String jsonString = cs.getString(columnIndex);
        return jsonString != null ? JSON.parseObject(jsonString) : null;
    }
}
