package com.avasthi.datascience.pipeline.server.utils;


import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinitionSchema;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.server.caching.InputSourceCachingService;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionEntity;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionSchemaEntity;
import com.avasthi.datascience.pipeline.server.entities.InputSourceEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ObjectConverter {
    @Autowired
    private InputSourceCachingService inputSourceCachingService;

    public InputSource convert(InputSourceEntity source)  {
            return InputSource.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .type(source.getType())
                    .dbName(source.getDbName())
                    .hostname(source.getHostname())
                    .password(source.getPassword())
                    .username(source.getUsername())
                    .portNumber(source.getPortNumber())
                    .build();
    }
    public Set<DatasetDefinition> convert(Set<DatasetDefinitionEntity> source) {
        return source
                .stream()
                .map(e -> convert(e))
                .collect(Collectors.toSet());
    }
    public DatasetDefinition convert(DatasetDefinitionEntity source)  {
        try {

            return DatasetDefinition.builder()
                    .id(source.getId())
                    .type(source.getType())
                    .name(source.getName())
                    .inputSourceId(source.getInputSourceId())
                    .inputSource(convert(inputSourceCachingService.findById(source.getInputSourceId()).get()))
                    .tableName(source.getTableName())
                    .query(source.getQuery())
                    .dependentOn(convert(source.getDependentOn()))
                    .build();
        }
        catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            log.error("Conversion error", e);
            throw new IllegalArgumentException(String.format("Conversion Error", e));
        }
    }
    public DatasetDefinitionSchema convert(DatasetDefinitionSchemaEntity source)  {
            return DatasetDefinitionSchema.builder()
                    .id(source.getId())
                    .name(source.getName())
                    .datasetId(source.getDatasetId())
                    .dataType(source.getDataType())
                    .metadata(source.getMetadata())
                    .build();
        }
}
