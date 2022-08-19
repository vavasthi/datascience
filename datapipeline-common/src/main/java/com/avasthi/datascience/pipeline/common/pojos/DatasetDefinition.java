package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class DatasetDefinition {
    private UUID id;
    private String name;
    private UUID inputSourceId;
    private InputSource inputSource;
    private DatasetType type;
    private String tableName;
    private String query;
    private Set<DatasetDefinition> dependentOn;
}
