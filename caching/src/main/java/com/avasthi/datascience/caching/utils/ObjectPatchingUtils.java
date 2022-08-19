package com.avasthi.datascience.caching.utils;

import com.avasthi.datascience.caching.annotations.SkipPatching;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectPatchingUtils {
    /**
     * This method can be used to copy attribute values from class of one type into another type. It depends on existence
     * of getter and setter methods. If a getter method is present and is not skipped by SkipPatching annotation and a
     * matching setter method exists in destination class, the value is copied. If the source value is null then it is not
     * copied.
     *
     * @param destination
     * @param source
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void diffAndPatch(Object destination,
                                    Object source)
            throws InvocationTargetException, IllegalAccessException {
        Map<String, Method> getterMethods = new HashMap<>();
        Map<String, Method> setterMethods = new HashMap<>();
        for (Method m : source.getClass().getMethods()) {
            if (m.getGenericParameterTypes().length == 0 &&
                    m.getName().length() > 3 &&
                    m.getName().startsWith("get")) {
                getterMethods.put(m.getName(), m);

            }
        }
        for (Method m : destination.getClass().getMethods()) {
            if (m.getGenericParameterTypes().length == 1 &&
                    m.getName().length() > 3 &&
                    m.getName().startsWith("set")) {
                setterMethods.put(m.getName(), m);
            }
        }
        for (Map.Entry<String, Method> e : getterMethods.entrySet()) {

            Annotation[] annotations = e.getValue().getAnnotations();
            boolean skipped = false;
            for (Annotation a : annotations) {
                if (a.annotationType().equals(SkipPatching.class)) {
                    skipped = true;
                }
            }

            if (!skipped) {

                Object sourceValue = e.getValue().invoke(source);
                if (sourceValue != null) {

                    String setterMethodName = "set" + e.getValue().getName().substring(new String("get").length());
                    Method m = setterMethods.get(setterMethodName);
                    if (m != null) {
                        m.invoke(destination, sourceValue);
                    }
                }
            }
        }
    }
}