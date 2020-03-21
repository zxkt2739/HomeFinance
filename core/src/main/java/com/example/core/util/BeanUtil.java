package com.example.core.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


/**
 * @author faker
 */
public class BeanUtil {

    public static <T> T copyProperties(Object source, T target) {

        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * Map转Object
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<Object, Object> map, Class beanClass) throws Exception {
        if (map == null){
            return null;
        }
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(obj, map.get(field.getName()));
            }
        }
        return obj;
    }
}