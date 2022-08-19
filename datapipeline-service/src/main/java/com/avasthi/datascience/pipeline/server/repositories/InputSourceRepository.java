package com.avasthi.datascience.pipeline.server.repositories;

import com.avasthi.datascience.pipeline.server.entities.InputSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InputSourceRepository extends JpaRepository<InputSourceEntity, UUID> {
}
