package com.avasthi.datascience.pipeline.server.caching;

import com.avasthi.datascience.caching.annotations.DefineCache;
import com.avasthi.datascience.caching.exceptions.EntityDoesnotExist;
import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.caching.utils.ObjectPatchingUtils;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionEntity;
import com.avasthi.datascience.pipeline.server.repositories.DatasetRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@DefineCache(name = Constants.CACHES.DATASET_CACHE_NAME,
        prefix = Constants.CACHES.DATASET_CACHE_PREFIX,
        expiry = Constants.Times.HALF_HOUR)
public class DatasetDefinitionCachingService extends EntityCachingService<UUID, DatasetDefinitionEntity>{
    @Autowired
    private DatasetRepository datasetRepository;

    public Optional<DatasetDefinitionEntity> findById(UUID id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Optional<DatasetDefinitionEntity> optionalDatasetEntity = findById(id, DatasetDefinitionEntity.class, DatasetRepository.class);
        if (!optionalDatasetEntity.isPresent()) {
            optionalDatasetEntity = datasetRepository.findById(id);
            if (optionalDatasetEntity.isPresent()) {
                return optionalDatasetEntity;
            }
            else {
                throw EntityDoesnotExist.builder()
                        .errorCode(HttpStatus.NOT_FOUND_404)
                        .message(String.format("Entity with id %s not found", id))
                        .build();
            }
        }
        return optionalDatasetEntity;
    }

    @Override
    public Iterable<DatasetDefinitionEntity> findAll() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return datasetRepository.findAll();}

    @Override
    public Optional<DatasetDefinitionEntity> evict(UUID id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return Optional.empty();
    }

    @Override
    public Iterable<KeyPrefixForCache> getAllKeys(UUID id, List<KeyPrefixForCache> keys) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return null;
    }

    @Override
    public Optional<DatasetDefinitionEntity> delete(UUID id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return Optional.empty();
    }

    @Override
    public Optional<DatasetDefinitionEntity> create(DatasetDefinitionEntity entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        entity = datasetRepository.save(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<DatasetDefinitionEntity> update(UUID id, DatasetDefinitionEntity entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Optional<DatasetDefinitionEntity> optionalDatasetEntity
                 = datasetRepository.findById(id);
        if (optionalDatasetEntity.isPresent()) {
            DatasetDefinitionEntity savedEntity = optionalDatasetEntity.get();
            ObjectPatchingUtils.diffAndPatch(savedEntity, entity);
        }
        return Optional.empty();
    }
}
