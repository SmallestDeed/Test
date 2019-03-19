package com.sandu.search.common.tools;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JSON转换类
 *
 * @date 20171211
 * @auth pengxuangang
 */
@Slf4j
public class JsonUtil {

    private final static String CLASS_LOG_PREFIX = "JSON转换类:";

    private static Gson GSON = null;

    private static GsonBuilder notEmptyBuilder = null;

    static {
        //普通GSON
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        GSON = builder.create();

        //非空GsonBuilder
        notEmptyBuilder = new GsonBuilder();
        notEmptyBuilder.registerTypeAdapter(Date.class, new DateAdapter());
        notEmptyBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 对象转JSON(去空)
     *
     * @param obj 待转换对象
     * @return
     */
    public static String toJsonExcludeEmpty(Object obj) {

        //查询有值字段
        List<String> fieldList;
        try {
            fieldList = EntityUtil.queryHasValueExcludeNameIsIdInField(obj);
        } catch (IllegalAccessException e) {
            log.error(CLASS_LOG_PREFIX + "查询对象中有值字段失败! obj:{}.", obj);
            return null;
        }

        //排除其余字段
        List<Field> excludeFieldList = EntityUtil.queryExcludeFieldInObject(obj, fieldList);
        List<FieldAttributes> fieldAttributesList = new ArrayList<>(excludeFieldList.size());
        excludeFieldList.forEach(field -> fieldAttributesList.add(new FieldAttributes(field)));

        //创建字段排除策略
        ExclusionStrategy exclusionStrategy = new SpecificClassExclusionStrategy(obj.getClass());
        //应用策略
        fieldAttributesList.forEach(exclusionStrategy::shouldSkipField);

        //创建对象
        Gson gson = notEmptyBuilder.create();

        return gson.toJson(obj);
    }

    /**
     * 对象转JSON
     *
     * @param obj 待转换对象
     * @return
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * JSON转对象
     *
     * @param jsonStr  待转换字符串
     * @param objClass 类Class
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonStr, Class<T> objClass) {
        return GSON.fromJson(jsonStr, objClass);
    }

    /**
     * JSON转List对象
     *
     * @param jsonStr 待转换字符串
     * @param typeOfT 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String jsonStr, Type typeOfT) {
        return GSON.fromJson(jsonStr, typeOfT);
    }

}

/**
 * JSON对象日期适配器
 *
 * @date 20171228
 * @auth pengxuangang
 */
class DateAdapter implements JsonDeserializer<Date> {

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonElement arg0, Type arg1,
                            JsonDeserializationContext arg2) throws JsonParseException {
        try {
            return df.parse(arg0.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class SpecificClassExclusionStrategy implements ExclusionStrategy {

    private final Class<?> excludedThisClass;

    public SpecificClassExclusionStrategy(Class<?> excludedThisClass) {
        this.excludedThisClass = excludedThisClass;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return excludedThisClass.equals(clazz);
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return excludedThisClass.equals(f.getDeclaredClass());
    }
}

