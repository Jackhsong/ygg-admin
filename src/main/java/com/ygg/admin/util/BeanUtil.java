package com.ygg.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lorabit
 * @since 16-4-15
 * 对象之间转换工具类
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    public static <T, E> List<E> copyPropertiesArrayList(List<T> source, Class<E> target) {
        return copyPropertiesArrayList(source, target, null);
    }

    public static <T, E> List<E> copyPropertiesArrayList(List<T> source, Class<E> target,
                                                         String[] ignoreProperties) {
        List<E> es = new ArrayList<E>();
        try {
            if (CollectionUtils.isNotEmpty(source)) {
                for (T t : source) {
                    E res = target.newInstance();
                    BeanUtils.copyProperties(t, res, ignoreProperties);
                    es.add(res);
                }
            }
        } catch (Exception e) {
            logger.warn(MessageFormatter.arrayFormat("copy properties failed, source={}, target={}, e",
                    new Object[]{source.getClass(), target.getClass(), e.getMessage()}).getMessage(), e);
        }

        return es;
    }

    public static <T , E > E copyProperties(T source,
                                                                                    Class<E> target) {
        return copyProperties(source, target, null);
    }


    public static <T , E > E copyProperties(T source,
                                                                                    Class<E> target, String[] ignorePropertie) {
        if (source == null) {
            return null;
        }

        try {
            if (ignorePropertie == null || ignorePropertie.length == 0) {
                return JSON.parseObject(JSON.toJSONString(source), target);
            } else {
                SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
                for (String ignore : ignorePropertie) {
                    filter.getExcludes().add(ignore);
                }
                return JSON.parseObject(JSON.toJSONString(source, filter), target);
            }
        } catch (Exception e) {
            logger.warn(MessageFormatter.arrayFormat("copy properties failed, source={}, target={}, e",
                    new Object[]{source.getClass(), target.getClass(), e.getMessage()}).getMessage(), e);
        }
        return null;
    }

}
