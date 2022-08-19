package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DatasetDefinitionSchemaCreatePojo {
    private UUID datasetId;
    private String name;
    private String dataType;
    private String metadata;
}
