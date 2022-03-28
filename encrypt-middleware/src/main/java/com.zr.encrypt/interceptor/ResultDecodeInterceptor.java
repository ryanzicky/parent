package com.zr.encrypt.interceptor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zr.encrypt.annotation.EncryptHandleInfo;
import com.zr.encrypt.entity.DecodeEntity;
import com.zr.encrypt.strategy.IDecryptResultFieldStrategy;
import com.zr.encrypt.strategy.IEncryptResultFieldStrategy;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 通过拦截器对返回结果中的某个字段进行加密处理
 */
@Intercepts(
        {
                @Signature(
                        type = ResultSetHandler.class,
                        method = "handleResultSets",
                        args = {Statement.class}
                )
        }
)
@Component
public class ResultDecodeInterceptor implements Interceptor, ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取到返回结果
        Object returnValue = invocation.proceed();
        if (returnValue == null) {
            return returnValue;
        }
        try {
            if (returnValue instanceof ArrayList<?>) {
                List<?> list = (ArrayList<?>) returnValue;
                if (CollectionUtils.isEmpty(list)) {
                    return returnValue;
                }
                Object firstRes = list.get(0);
                Map<Field, Class<? extends IDecryptResultFieldStrategy>> strategyMap = getDecodeStrategy(firstRes.getClass());

                if (MapUtils.isEmpty(strategyMap)) {
                    return returnValue;
                }
                handleEncrypt(list, strategyMap);
            } else {
                Map<Field, Class<? extends IDecryptResultFieldStrategy>> strategyMap = getDecodeStrategy(returnValue.getClass());
                if (MapUtils.isEmpty(strategyMap)) {
                    return returnValue;
                }
                handleDecrypt(strategyMap, returnValue);
            }
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
        return returnValue;
    }

    private void handleDecrypt(Map<Field, Class<? extends IDecryptResultFieldStrategy>> strategyMap, Object innerRes) throws Exception {
        for (Map.Entry<Field, Class<? extends IDecryptResultFieldStrategy>> entry : strategyMap.entrySet()) {
            PropertyDescriptor pd = new PropertyDescriptor(entry.getKey().getName(), innerRes.getClass());
            Method rM = pd.getReadMethod();
            Object o = rM.invoke(innerRes);
            if (o == null) {
                continue;
            }
            IDecryptResultFieldStrategy bean = applicationContext.getBean(entry.getValue());
            List<String> decodePhones = bean.decode(Lists.newArrayList(o.toString()));
            if (CollectionUtils.isEmpty(decodePhones)) {
                return;
            }
            Method wM = pd.getWriteMethod();
            wM.invoke(innerRes, decodePhones.get(0));
        }
    }

    private void handleEncrypt(List<?> list, Map<Field, Class<? extends IDecryptResultFieldStrategy>> strategyMap) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        ArrayListMultimap<IDecryptResultFieldStrategy, DecodeEntity> strategyEntityMap = ArrayListMultimap.create();

        for (Map.Entry<Field, Class<? extends IDecryptResultFieldStrategy>> entry : strategyMap.entrySet()) {
            PropertyDescriptor pd = new PropertyDescriptor(entry.getKey().getName(), list.get(0).getClass());
            Method rM = pd.getReadMethod();

            IDecryptResultFieldStrategy strategy = applicationContext.getBean(entry.getValue());
            for (Object o : list) {
                Object c = rM.invoke(o);
                if (c == null) {
                    continue;
                }
                DecodeEntity decodeEntity = new DecodeEntity();
                decodeEntity.setObj(o);
                decodeEntity.setField(entry.getKey());
                decodeEntity.setEncode(c.toString());

                strategyEntityMap.put(strategy, decodeEntity);
            }
        }
        for (IDecryptResultFieldStrategy strategy : strategyEntityMap.keySet()) {
            List<DecodeEntity> decodeEntities = strategyEntityMap.get(strategy);
            if (CollectionUtils.isEmpty(decodeEntities)) {
                continue;
            }
            List<String> decode = strategy.decode(decodeEntities.stream().map(c -> c.getEncode()).collect(Collectors.toList()));
            for (int i = 0; i < decodeEntities.size(); i ++) {
                DecodeEntity decodeEntity = decodeEntities.get(i);

                PropertyDescriptor pd = new PropertyDescriptor(decodeEntity.getField().getName(), decodeEntity.getObj().getClass());
                Method wM = pd.getWriteMethod();
                wM.invoke(decodeEntity.getObj(), decode.get(i));
            }
        }

    }

    public static Map<Field, Class<? extends IEncryptResultFieldStrategy>> getEncodeStrategy(Class clasz) {
        Field[] declaredFields = clasz.getDeclaredFields();
        Map<Field, Class<? extends IEncryptResultFieldStrategy>> strategyMap = Maps.newHashMap();
        for (Field f : declaredFields) {
            EncryptHandleInfo an = f.getDeclaredAnnotation(EncryptHandleInfo.class);
            if (an == null) {
                continue;
            }
            if (an.encryptStrategy() != null) {
                strategyMap.put(f, an.encryptStrategy());
            }
        }
        return strategyMap;
    }

    public static Map<Field, Class<? extends IDecryptResultFieldStrategy>> getDecodeStrategy(Class clasz) {
        Field[] declaredFields = clasz.getDeclaredFields();
        Map<Field, Class<? extends IDecryptResultFieldStrategy>> strategyMap = Maps.newHashMap();
        for (Field f : declaredFields) {
            EncryptHandleInfo an = f.getDeclaredAnnotation(EncryptHandleInfo.class);
            if (an == null) {
                continue;
            }
            if (an.encryptStrategy() != null) {
                strategyMap.put(f, an.decodeStrategy());
            }
        }
        return strategyMap;
    }

    public static Map<String, Class<? extends IEncryptResultFieldStrategy>> getWrapperStrategy(Class clasz) {
        Field[] declaredFields = clasz.getDeclaredFields();
        Map<String, Class<? extends IEncryptResultFieldStrategy>> strategyMap = Maps.newHashMap();
        for (Field f : declaredFields) {
            EncryptHandleInfo an = f.getDeclaredAnnotation(EncryptHandleInfo.class);
            if (an == null) {
                continue;
            }
            if (an.encryptStrategy() != null) {
                strategyMap.put(an.wrapperName(), an.encryptStrategy());
            }
        }
        return strategyMap;
    }

    public static Map<String, Class<? extends IEncryptResultFieldStrategy>> getParamStrategy(Class clasz) {
        Field[] declaredFields = clasz.getDeclaredFields();
        Map<String, Class<? extends IEncryptResultFieldStrategy>> strategyMap = Maps.newHashMap();
        for (Field f : declaredFields) {
            EncryptHandleInfo an = f.getDeclaredAnnotation(EncryptHandleInfo.class);
            if (an == null) {
                continue;
            }
            if (an.encryptStrategy() != null) {
                strategyMap.put(an.paramName(), an.encryptStrategy());
            }
        }
        return strategyMap;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
