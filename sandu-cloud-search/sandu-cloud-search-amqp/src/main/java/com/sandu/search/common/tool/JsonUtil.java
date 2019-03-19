package com.sandu.search.common.tool;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JSON转换类
 *
 * @date 20171211
 * @auth pengxuangang
 */
public class JsonUtil {

    private static Gson GSON = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        GSON = builder.create();
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
