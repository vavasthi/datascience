
package com.avasthi.datascience.pipeline.server.caching;

import com.avasthi.datascience.caching.pojos.CachedPojo;
import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.caching.service.AbstractCacheService;
import com.avasthi.datascience.pipeline.server.configurations.EntityCacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public abstract class EntityCachingService<I, E extends CachedPojo<I>> extends AbstractCacheService<I, E> {

    @Autowired
    private EntityCacheConfig redisConfiguration;

    protected <V> V getObject(Object key,
                              Class<V> vClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return vClass.cast(getObject(redisConfiguration.redisTemplate(), key));
    }
    protected void storeObject(Object key,
                               Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        storeObject(redisConfiguration.redisTemplate(), key, value);
    }
    protected void storeList(Object key,
                             List<E> value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        storeObject(redisConfiguration.redisTemplate(), key, value);
    }
    protected List<E> getList(Object key)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getList(redisConfiguration.redisTemplate(), key);
    }
    protected Optional<E> evictObject(I id,
                                      Class<E> entityClass,
                                      Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return evictObject(redisConfiguration.redisTemplate(), id, entityClass, repositoryClass);
    }
    public Optional<E> findById(I id,
                                Class<E> entityClass,
                                Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return findById(redisConfiguration.redisTemplate(), id, entityClass, repositoryClass);
    }

    public Optional<E> delete(I id,
                              Class<E> entityClass,
                              Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return delete(redisConfiguration.redisTemplate(), id, entityClass, repositoryClass);
    }

    public Optional<E> create(E entity,
                              Class<E > entityClass,
                              Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return create(redisConfiguration.redisTemplate(), entity, entityClass, repositoryClass);
    }
    public Optional<E> update(I id,
                              E entity,
                              Class<E> entityClass,
                              Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        return update(redisConfiguration.redisTemplate(), id, entity, entityClass, repositoryClass);
    }
    public Iterable<KeyPrefixForCache> getAllKeys(I id,
                                                  List<KeyPrefixForCache> keys,
                                                  Class<E> entityClass,
                                                  Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getAllKeys(redisConfiguration.redisTemplate(), id, keys, entityClass, repositoryClass);

    }
    protected <I, E> Map<I, E>  storeValuesToHash(Object key,
                                                  Map<I, E> value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return storeValuesToHash(redisConfiguration.redisTemplate(), key, value);
    }
    protected <RV> Set<RV> getValuesFromHash(Object key,
                                             Class<RV> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getValuesFromHash(redisConfiguration.redisTemplate(), key, clazz);
    }
    protected <RV> RV getValueFromHash(Object key,
                                       Object hashKey,
                                       Class<RV> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getValueFromHash(redisConfiguration.redisTemplate(), key, hashKey, clazz);
    }
    protected void evictKeySet(Object key)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        evictKeySet(redisConfiguration.redisTemplate(), key);
    }
    protected Optional<E> findByKey(String scoPathKey,
                                    List keyValues,
                                    Class<E> entityClass,
                                    Class<? extends CrudRepository> repositoryClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return findByKey(redisConfiguration.redisTemplate(),
                scoPathKey,
                keyValues,
                entityClass,
                repositoryClass);
    }

}