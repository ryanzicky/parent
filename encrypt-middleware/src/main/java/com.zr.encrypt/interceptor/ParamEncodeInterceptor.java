package com.zr.encrypt.interceptor;

import com.baomidou.mybatisplus.mapper.SqlPlus;
import com.google.common.collect.Maps;
import com.zr.encrypt.entity.EntityWrapper;
import com.zr.encrypt.strategy.IEncryptResultFieldStrategy;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * @Author zhourui
 * @Date 2021/10/12 20:40
 */
@Intercepts({
        //执行参数接口，method为接口名称，args为参数对象（注意：不同版本个数不同，该版本：5.0.0）
        @Signature(type= ParameterHandler.class, method="setParameters", args={PreparedStatement.class})
//        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
public class ParamEncodeInterceptor implements Interceptor, ApplicationContextAware {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //反射获取一堆的mybatis内部对象
        Class<?> clazz  = invocation.getTarget().getClass();
        ParameterHandler parameterHandler = null;
        if (clazz.getName().contains("$Proxy")) {
            clazz = clazz.getSuperclass();
            Field hField = clazz.getDeclaredField("h");
            hField.setAccessible(true);
            Plugin h = (Plugin) hField.get(invocation.getTarget());

            Field target = h.getClass().getDeclaredField("target");
            target.setAccessible(true);
            Object ob = target.get(h);
            if (ob.getClass().getName().contains("$Proxy")) {
                clazz = ob.getClass().getSuperclass();
                hField = clazz.getDeclaredField("h");
                hField.setAccessible(true);
                h = (Plugin) hField.get(ob);
                target = h.getClass().getDeclaredField("target");
                target.setAccessible(true);
                parameterHandler = (ParameterHandler) target.get(h);
            }
        } else {
            parameterHandler = (ParameterHandler) invocation.getTarget();
        }
        Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
        boundSqlField.setAccessible(true);
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        Field mappedStatement = parameterHandler.getClass().getDeclaredField("mappedStatement");
        parameterField.setAccessible(true);
        mappedStatement.setAccessible(true);
        Object parameterObject = parameterField.get(parameterHandler);
        if (null == parameterObject) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement)mappedStatement.get(parameterHandler);
        Type genericInterface = Class.forName(ms.getId().substring(0, ms.getId().lastIndexOf("."))).getGenericInterfaces()[0];
        ParameterizedType p = (ParameterizedType)genericInterface;
        Class type = (Class) p.getActualTypeArguments()[0];

        //获取类上面的EncryptResultFieldAnnotation注解相关信息
        Map<String, Class<? extends IEncryptResultFieldStrategy>> wrapperStrategy = ResultDecodeInterceptor.getWrapperStrategy(type);
        Map<String, Class<? extends IEncryptResultFieldStrategy>> paramStrategyMap = ResultDecodeInterceptor.getParamStrategy(type);
        if (MapUtils.isEmpty(wrapperStrategy)) {
            return invocation.proceed();
        }
        Map<Integer, Class<? extends IEncryptResultFieldStrategy>> mapField = Maps.newHashMap();
        if (parameterObject instanceof Map) {
            Map<String, Object> paramMap = (Map) parameterObject;

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                encodeEntityWrapper(wrapperStrategy, mapField, entry);
                encodeBaseParam(paramMap, paramStrategyMap, entry);
                encodeBeanDAOParam(entry.getValue());
            }
        } else {       //传参如果是对象形式
            encodeBeanDAOParam(parameterObject);
        }

        parameterField.set(parameterHandler, parameterObject);

        return invocation.proceed();
    }

    private void encodeEntityWrapper(Map<String, Class<? extends IEncryptResultFieldStrategy>> wrapperStrategy, Map<Integer, Class<? extends IEncryptResultFieldStrategy>> mapField, Map.Entry<String, Object> entry) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (!(entry.getValue() instanceof EntityWrapper)) {
            return;
        }
        if (!StringUtils.equals(entry.getKey(), "ew")) {
            return;
        }
        EntityWrapper wrapper = (EntityWrapper) entry.getValue();
        Field sqlField = wrapper.getClass().getSuperclass().getDeclaredField("sql");
        sqlField.setAccessible(true);
        SqlPlus plus = (SqlPlus) sqlField.get(wrapper);

        Field sqlConditionField = plus.getClass().getSuperclass().getDeclaredField("sql");
        sqlConditionField.setAccessible(true);
        Object sqlCondition = sqlConditionField.get(plus);

        Field whereField = sqlCondition.getClass().getDeclaredField("where");
        whereField.setAccessible(true);
        List<String> wheres = (List<String>) whereField.get(sqlCondition);

        for (String subQuery : wheres) {
            Matcher m = compile("(\\d+)").matcher(subQuery);
            int matcher_start = 0;
            while (m.find(matcher_start)){
                Class<? extends IEncryptResultFieldStrategy> subStrategy = wrapperStrategy.get(subQuery.split(" ")[0]);
                if (subStrategy != null) {
                    mapField.put(Integer.parseInt(m.group(1)),subStrategy);
                }
                matcher_start = m.end();
            }
        }
        if (MapUtils.isEmpty(mapField)) {
            return ;
        }
        Field paramNameValuePairsField = wrapper.getClass().getSuperclass().getDeclaredField("paramNameValuePairs");
        paramNameValuePairsField.setAccessible(true);
        Map<String, Object> pairMap = (Map<String, Object>) paramNameValuePairsField.get(wrapper);
        for (Map.Entry<Integer, Class<? extends IEncryptResultFieldStrategy>> integerClassEntry : mapField.entrySet()) {
            String key = "MPGENVAL" + integerClassEntry.getKey();
            IEncryptResultFieldStrategy bean = applicationContext.getBean(integerClassEntry.getValue());
            String encrypt = bean.encrypt(pairMap.get(key).toString());

            pairMap.put(key, encrypt);
        }

    }

    private void encodeBaseParam(Map<String, Object> paramMap, Map<String, Class<? extends IEncryptResultFieldStrategy>> paramStrategyMap, Map.Entry<String, Object> entry) {
        if (!paramStrategyMap.containsKey(entry.getKey())) {
            return;
        }
        if (entry.getValue() == null) {
            return;
        }
        String encrypt = applicationContext.getBean(paramStrategyMap.get(entry.getKey())).encrypt(entry.getValue() + "");
        paramMap.put(entry.getKey(), encrypt);
    }

    private void encodeBeanDAOParam(Object parameterObject) throws InvocationTargetException, IllegalAccessException, IntrospectionException {
        Map<Field, Class<? extends IEncryptResultFieldStrategy>> encryptStrategy = ResultDecodeInterceptor.getEncodeStrategy(parameterObject.getClass());
        if (MapUtils.isEmpty(encryptStrategy)) {
            return ;
        }

        for (Map.Entry<Field, Class<? extends IEncryptResultFieldStrategy>> entry : encryptStrategy.entrySet()) {
            PropertyDescriptor pd = new PropertyDescriptor(entry.getKey().getName(), parameterObject.getClass());
            Method rM = pd.getReadMethod();
            Object o = rM.invoke(parameterObject);
            if (o == null) {
                continue;
            }
            IEncryptResultFieldStrategy bean = applicationContext.getBean(entry.getValue());
            String encodePhone = bean.encrypt(o.toString());

            Method wM = pd.getWriteMethod();
            wM.invoke(parameterObject, encodePhone);
        }
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
