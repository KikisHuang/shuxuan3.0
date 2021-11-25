package com.kikis.commnlibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;


/**
 * Created by lian on 2018/1/8.
 */
public class GsonUtil {

    private static final String TAG = getTAG(GsonUtil.class);
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = getIntGson();
        }
    }

    private GsonUtil() {

    }

    /**
     * 解决gson默认将int转换为double
     *
     * @return
     */
    public static Gson getIntGson() {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    @Override
                    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                        if (src == src.longValue())
                            return new JsonPrimitive(src.longValue());
                        return new JsonPrimitive(src);
                    }
                }).create();
        return gson;
    }


    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String gsonToStr(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }


/*    *//**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     *
     * @param gsonString
     * @param cls
     * @return
     *//*
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }*/


    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成倒序list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToInvertedList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (int i = array.size() - 1; i >= 0; i--) {
            list.add(gson.fromJson(array.get(i), cls));

        }
       /* for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }*/
        return list;
    }

    /**
     * 转成倒序LinkedList
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> LinkedList<T> jsonToInvertedLinkedList(String json, Class<T> cls) {
        Gson gson = new Gson();
        LinkedList<T> list = new LinkedList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (int i = array.size() - 1; i >= 0; i--) {
            list.add(gson.fromJson(array.get(i), cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }


    public static Map<String, String> getJsonMap() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public static Map<String, Object> getObjMap() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
