package com.avasthi.datascience.caching.annotations;



import com.avasthi.datascience.caching.service.AbstractCacheService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in class only.
public @interface RelatedCache {
    Class<? extends AbstractCacheService> clazz();
    String primaryKeyField();
}