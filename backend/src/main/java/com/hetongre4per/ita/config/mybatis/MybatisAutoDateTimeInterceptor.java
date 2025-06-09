package com.hetongre4per.ita.config.mybatis;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MybatisAutoDateTimeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = statement.getId();
        log.debug("sqlId:{}", sqlId);
        SqlCommandType statementType = statement.getSqlCommandType();
        log.debug("sqlCommandType:{}", statementType);
        Object param = invocation.getArgs()[1];
        if (Objects.isNull(param)) {
            return invocation.proceed();
        }
        List<Field> fields;
        if (SqlCommandType.INSERT == statementType) {
            fields = getAllField(param);
            for (Field field : fields) {
                if (StrUtil.equals(field.getName(), "createdTime") || StrUtil.equals(field.getName(), "updatedTime")) {
                    field.setAccessible(true);
                    Object currentFieldValue = field.get(param);
                    if (Objects.isNull(currentFieldValue) || "".equals(currentFieldValue)) {
                        field.set(param, new Date());
                    }
                    field.setAccessible(false);
                }
            }
        } else if (SqlCommandType.UPDATE == statementType) {
            if (param instanceof MapperMethod.ParamMap<?>) {
                MapperMethod.ParamMap<?> paramMap = (MapperMethod.ParamMap<?>) param;
                if (paramMap.containsKey("et")) {
                    param = paramMap.get("et");
                } else {
                    param = paramMap.get("param1");
                }
                if (Objects.isNull(param)) {
                    return invocation.proceed();
                }
            }
            fields = getAllField(param);
            
            for (Field field : fields) {
                if (StrUtil.equals(field.getName(), "updatedTime")) {
                    field.setAccessible(true);
                    Object currentFieldValue = field.get(param);
                    if (Objects.isNull(currentFieldValue) || "".equals(currentFieldValue)) {
                        field.set(param, new Date());
                    }
                    field.setAccessible(false);
                }
            }
        }
        return invocation.proceed();
    }
    
    private List<Field> getAllField(Object param) {
        ArrayList<Field> list = new ArrayList<>();
        Class<?> clazz = param.getClass();
        while (clazz != null) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return list;
    }
    
}
