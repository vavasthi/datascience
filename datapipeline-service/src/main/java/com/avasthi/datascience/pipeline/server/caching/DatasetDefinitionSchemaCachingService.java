package com.avasthi.datascience.pipeline.server.caching;

import com.avasthi.datascience.caching.annotations.DefineCache;
import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.configurations.DatasetSchemaCacheConfig;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionSchemaEntity;
import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinitionSchemaCreatePojo;
import com.avasthi.datascience.pipeline.server.repositories.DatasetSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@DefineCache(name = Constants.CACHES.DATASET_SCHEMA_CACHE_NAME,
        prefix = Constants.CACHES.DATASET_SCHEMA_CACHE_PREFIX,
        expiry = Constants.Times.HALF_HOUR)
public class DatasetDefinitionSchemaCachingService {

    @Autowired
    private DatasetSchemaCacheConfig redisConfiguration;

    @Autowired
    private DatasetSchemaRepository datasetSchemaRepository;

    public List<DatasetDefinitionSchemaEntity> findAll(UUID id)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        KeyPrefixForCache kpfc = new KeyPrefixForCache(getPrefix(), id);
        long size = redisConfiguration.redisTemplate().opsForList().size(kpfc);
        if (size == 0) {

            List<DatasetDefinitionSchemaEntity> datasetDefinitionEntities = datasetSchemaRepository.findByDatasetId(id);
            redisConfiguration.redisTemplate().opsForList().leftPushAll(kpfc, datasetDefinitionEntities);
            return datasetDefinitionEntities;
        }
        else {

            return redisConfiguration.redisTemplate()
                    .opsForList()
                    .range(kpfc, 0, size - 1);
        }
    }

    public List<DatasetDefinitionSchemaEntity> delete(UUID id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<DatasetDefinitionSchemaEntity> datasetDefinitionSchemaEntities = findAll(id);
        for (DatasetDefinitionSchemaEntity ddse : datasetDefinitionSchemaEntities) {
            delete(ddse.getId());
        }
        redisConfiguration.redisTemplate().delete(new KeyPrefixForCache(getPrefix(), id));
        return datasetDefinitionSchemaEntities;
    }

    public List<DatasetDefinitionSchemaEntity> create(List<DatasetDefinitionSchemaCreatePojo> entities) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return datasetSchemaRepository.saveAll(entities.stream().map(e -> new DatasetDefinitionSchemaEntity(UUID.randomUUID(),
                e.getDatasetId(),
                e.getName(),
                e.getDataType(),
                e.getMetadata()))
                .collect(Collectors.toList())
        );
    }

    public List<DatasetDefinitionSchemaEntity> update(UUID id, List<DatasetDefinitionSchemaEntity> entities) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        delete(id);
        return datasetSchemaRepository.saveAll(entities);
    }
    private long getExpiry() {

        return this.getClass().getAnnotation(DefineCache.class).expiry();
    }

    private String getPrefix() {

        return this.getClass().getAnnotation(DefineCache.class).prefix();
    }
}
