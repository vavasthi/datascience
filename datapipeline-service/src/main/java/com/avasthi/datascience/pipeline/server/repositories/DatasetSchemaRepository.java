package com.avasthi.datascience.pipeline.server.repositories;

import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionSchemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DatasetSchemaRepository extends JpaRepository<DatasetDefinitionSchemaEntity, UUID> {
    List<DatasetDefinitionSchemaEntity> findByDatasetId(UUID datasetId);
}
