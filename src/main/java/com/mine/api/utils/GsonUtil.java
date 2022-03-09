package com.mine.api.utils;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装的gson工具
 *
 * @author duanjunjie
 * 2020-03-05
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private static JsonParser jsonParser = null;

    static {
        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }
    }

    /**
     * json字符串转JsonObject
     *
     * @param str
     * @return
     */
    public static JsonObject strToJsonObject(String str) {
        return jsonParser.parse(str).getAsJsonObject();
    }

    /**
     * json字符串转JsonArray
     *
     * @param str
     * @return
     */
    public static JsonArray strToJsonArray(String str) {
        return jsonParser.parse(str).getAsJsonArray();
    }

    /**
     * json字符串转java对象
     *
     * @param json
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T jsonStrToBean(String json, Class<T> beanClass) {
        return gson.fromJson(json, beanClass);
    }

    /**
     * java对象转json字符串
     *
     * @param bean
     * @return
     */
    public static String beanToJsonStr(Object bean) {
        return gson.toJson(bean);
    }

    /**
     * 带时间字段的java对象转json字符串，指定时间格式
     * @param bean
     * @param timeFormat 时间格式：如"yyyy-MM-dd HH:mm:ss"，"yyyy-MM-dd'T'HH:mm:ss"
     * @return
     */
    public static String beanToJsonStr(Object bean,String timeFormat){
        Gson gson = new GsonBuilder()
                .setDateFormat(timeFormat)
                .create();
        return gson.toJson(bean);
    }

    /**
     * 取JsonArray中每个JsonObject的某个key的值，放到list中(String)
     *
     * @param array
     * @param key
     * @return
     */
    public static List jsonArrayKeyToList(JsonArray array, String key) {
        List vIds = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            vIds.add(array.get(i).getAsJsonObject().get(key).getAsString());
        }
        return vIds;
    }

    /**
     * 将jsonArrayToList转换成List<T> 对象
     * @param jsonArray
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArrayToList(JsonArray jsonArray, Class<T> beanClass) {
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            arrayList.add(jsonStrToBean(jsonArray.get(i).getAsJsonObject().toString(), beanClass));
        }
        return arrayList;
    }

    /**
     * java对象转json对象
     *
     * @param bean
     * @return jsonObject
     */
    public static JsonObject beanToJsonObject(Object bean) {
        String jsonStr = GsonUtil.beanToJsonStr(bean);
        return GsonUtil.strToJsonObject(jsonStr);
    }
}
