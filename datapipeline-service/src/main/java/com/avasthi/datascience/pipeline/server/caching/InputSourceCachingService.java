package com.avasthi.datascience.pipeline.server.caching;

import com.avasthi.datascience.caching.annotations.DefineCache;
import com.avasthi.datascience.caching.exceptions.EntityDoesnotExist;
import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.caching.utils.ObjectPatchingUtils;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.entities.InputSourceEntity;
import com.avasthi.datascience.pipeline.server.repositories.InputSourceRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@DefineCache(name = Constants.CACHES.INPUT_SOURCE_CACHE_NAME,
        prefix = Constants.CACHES.INPUT_SOURCE_CACHE_PREFIX,
        expiry = Constants.Times.HALF_HOUR)
public class InputSourceCachingService extends EntityCachingService<UUID, InputSourceEntity>{
    @Autowired
    private InputSourceRepository inputSourceRepository;

    public Optional<InputSourceEntity> findById(UUID id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Optional<InputSourceEntity> optionalInputSourceEntity = findById(id, InputSourceEntity.class, InputSourceRepository.class);
        if (!optionalInputSourceEntity.isPresent()) {
            optionalInputSourceEntity = inputSourceRepository.findById(id);
            if (optionalInputSourceEntity.isPresent()) {
                return optionalInputSourceEntity;
            }
            else {
                throw EntityDoesnotExist.builder()
                        .errorCode(HttpStatus.NOT_FOUND_404)
                        .message(String.format("Entity with id %s not found", id))
                        .build();
            }
        }
        return optionalInputSourceEntity;
    }

    @Override
    public Iterable<InputSourceEntity> findAll() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return inputSourceRepository.findAll();}

    @Override
    public Optional<InputSourceEntity> evict(UUID id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return Optional.empty();
    }

    @Override
    public Iterable<KeyPrefixForCache> getAllKeys(UUID id, List<KeyPrefixForCache> keys) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return null;
    }

    @Override
    public Optional<InputSourceEntity> delete(UUID id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return Optional.empty();
    }

    @Override
    public Optional<InputSourceEntity> create(InputSourceEntity entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        entity = inputSourceRepository.save(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<InputSourceEntity> update(UUID id, InputSourceEntity entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Optional<InputSourceEntity> optionalInputSourceEntity
                 = inputSourceRepository.findById(id);
        if (optionalInputSourceEntity.isPresent()) {
            InputSourceEntity savedEntity = optionalInputSourceEntity.get();
            ObjectPatchingUtils.diffAndPatch(savedEntity, entity);
        }
        return Optional.empty();
    }
}
