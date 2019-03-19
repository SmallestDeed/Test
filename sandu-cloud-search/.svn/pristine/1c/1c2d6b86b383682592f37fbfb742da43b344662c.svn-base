package com.sandu.search.common.tools;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象单元
 *
 * @date 2018/6/7
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Slf4j
public class EntityUtil {

    private final static String CLASS_LOG_PREFIX = "对象单元:";

    /**
     *  检查对象中是否只有ID字段有值
     * @param object
     * @return 有值字段List
     * @throws IllegalAccessException
     */
    public static List<String> queryHasValueExcludeNameIsIdInField(Object object) throws IllegalAccessException {

        if (null == object) {
            log.warn(CLASS_LOG_PREFIX + "检查对象中是否只有ID字段有值失败，object not must be null!");
            throw new IllegalAccessException(CLASS_LOG_PREFIX + "检查对象中是否只有ID字段有值失败，object not must be null!");
        }

        //字段集合
        List<String> fieldList = new ArrayList<>();

        //ID是否有值
        boolean idValueExistInField = false;

        for (Field field : object.getClass().getDeclaredFields()) {
            //访问
            field.setAccessible(true);

            //排除序列化字段
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }

            Object obj = field.get(object);

            if (null == obj) {
                continue;
            }

            if ("id".equals(field.getName())) {
                idValueExistInField = true;
            } else {
                fieldList.add(field.getName());
            }
        }

        return idValueExistInField && 0 < fieldList.size() ? fieldList : null;
    }

    /**
     * 查询对象中除字段列表中的字段(serialVersionUID不包含在此列)
     *
     * @date 2018/6/7
     * @auth pengxuangang
     * @mail xuangangpeng@gmail.com
     */
    public static List<Field> queryExcludeFieldInObject(Object object, List<String> fieldList) {

        if (null == object) {
            return null;
        }

        //剩余字段
        List<Field> newFieldList = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            //访问
            field.setAccessible(true);

            String fieldName = field.getName();

            //排除序列化字段
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }

            fieldList = null == fieldList ? new ArrayList<String>() : fieldList;

            if (!fieldList.contains(fieldName)) {
                newFieldList.add(field);
            }
        }

        return newFieldList;
    }

    //将对象中的空值设为0
    public static Object setEntityNullValueToZeroForintegerAndDouble(Object object) {

        if (null == object) {
            return null;
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            //访问
            field.setAccessible(true);

            Object obj;
            try {
                obj = field.get(object);
                /*
                不需要置零,移除此方法
                if (null == obj) {
                    if (obj instanceof Integer) {
                        field.set(object, 0);
                    } else if (obj instanceof Double) {
                        field.set(object, 0);
                    }
                }*/
            } catch (IllegalAccessException e) {
                log.error(CLASS_LOG_PREFIX + "将对象中的空值设为0失败!IllegalAccessException:{}.", e);
                return null;
            }
        }

        return object;
    }
}
