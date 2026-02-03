package com.sia.common.dbservice.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * SQL日志拦截器
 * 记录执行的SQL语句和参数
 *
 * @author sia
 * @date 2026/02/02
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        long startTime = System.currentTimeMillis();
        try {
            Object result = invocation.proceed();
            long duration = System.currentTimeMillis() - startTime;

            // 记录SQL日志
            log.debug("SQL执行: {} 耗时: {}ms", boundSql.getSql(), duration);
            if (log.isTraceEnabled()) {
                log.trace("SQL参数: {}", boundSql.getParameterObject());
            }

            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("SQL执行失败: {} 耗时: {}ms", boundSql.getSql(), duration, e);
            throw e;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里配置拦截器属性
    }
}
