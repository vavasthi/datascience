package com.avasthi.datascience.pipeline.server.repositories;

import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DatasetRepository extends JpaRepository<DatasetDefinitionEntity, UUID> {
}
