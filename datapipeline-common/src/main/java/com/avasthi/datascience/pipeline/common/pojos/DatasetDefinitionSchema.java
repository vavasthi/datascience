package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class DatasetDefinitionSchema {
    private UUID id;
    private UUID datasetId;
    private String name;
    private String dataType;
    private String metadata;
}
