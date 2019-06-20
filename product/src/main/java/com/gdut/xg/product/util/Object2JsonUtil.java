package com.gdut.xg.product.util;

import com.gdut.xg.product.annoation.NoAdd;
import org.apache.http.client.utils.DateUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lulu
 * @Date 2019/6/19 23:13
 */
public class Object2JsonUtil {


    public static Map<String, Object> ObjectToMap(Object m) throws IllegalAccessException {

        Map<String, Object> map = new HashMap<>();
        Field[] fields = m.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!f.isAnnotationPresent(NoAdd.class)) {
                map.put(f.getName(), f.get(m));
            }
        }
        return map;
    }

    public static <T> T MapToObject(Map<String, Object> map, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        Field[] fields = tClass.getDeclaredFields();
        T result = tClass.newInstance();
        for (Field f : fields) {
            f.setAccessible(true);
            if (!f.isAnnotationPresent(NoAdd.class)) {

                f.set(result, changeToRight(map.get(f.getName()), f));
            }
        }
        return result;
    }

    private static Object changeToRight(Object o, Field f) {
        Object result = null;
        switch (f.getType().getName()) {
            case "java.util.Date":
                    result = DateUtils.parseDate(o.toString());
                break;
            case "java.lang.Integer":
                result = Integer.valueOf(o.toString());
                break;
            case "java.lang.String":
                result = o.toString();
                break;
            case "long":
                result = (long) o;
                break;


        }
        return result;
    }


}
