package com.avasthi.datascience.caching.utils;



import com.avasthi.datascience.caching.pojos.ClassAttributePair;
import com.avasthi.datascience.caching.service.AbstractCacheService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CacheCascadeAnnotationDictionary {

    public final Map<Class<? extends AbstractCacheService>, Set<ClassAttributePair> > annotationMapWithTargetClass
            = new HashMap<>();
    public final Map<Class<? extends AbstractCacheService>, Set<ClassAttributePair> > annotationMapWithAnnotatedClass
            = new HashMap<>();
    public static CacheCascadeAnnotationDictionary INSTANCE = new CacheCascadeAnnotationDictionary();
    private CacheCascadeAnnotationDictionary() {
    }

    public void add(String attribute,
                    Class<? extends AbstractCacheService> annotatedClass,
                    Class<? extends AbstractCacheService> targetClass) {

        if (!annotationMapWithTargetClass.containsKey(targetClass)) {
            annotationMapWithTargetClass.put(targetClass, new HashSet<>());
        }
        if (!annotationMapWithAnnotatedClass.containsKey(annotatedClass)) {
            annotationMapWithAnnotatedClass.put(annotatedClass, new HashSet<>());
        }
        annotationMapWithTargetClass.get(targetClass).add(new ClassAttributePair(attribute, annotatedClass));
        annotationMapWithAnnotatedClass.get(annotatedClass).add(new ClassAttributePair(attribute, targetClass));
    }
    public Set<ClassAttributePair>
    getAnnotationsOnAnnotatedClass(Class<? extends AbstractCacheService> annotatedClass) {
        return annotationMapWithAnnotatedClass.get(annotatedClass);
    }
    public Set<ClassAttributePair>
    getAnnotationsOnTargetClass(Class<? extends AbstractCacheService> targetClass) {
        return annotationMapWithTargetClass.get(targetClass);
    }
}
