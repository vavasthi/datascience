package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DatasetDefinitionCreatePojo {
    @NonNull
    private String name;
    @NonNull
    private UUID inputSourceId;
    @NonNull
    private DatasetType type;
    @NonNull
    private String tableName;
    @NonNull
    private String query;
    private Set<UUID> dependentOn;
}
