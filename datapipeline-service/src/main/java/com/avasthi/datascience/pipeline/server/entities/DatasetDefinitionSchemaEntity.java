package com.avasthi.datascience.pipeline.server.entities;

import com.avasthi.datascience.caching.annotations.SkipPatching;
import com.avasthi.datascience.caching.pojos.CachedPojo;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="datasets_schema")
public class DatasetDefinitionSchemaEntity extends CachedPojo<UUID>  implements Serializable  {
    public DatasetDefinitionSchemaEntity() {
    }

    public DatasetDefinitionSchemaEntity(@NonNull UUID id,
                                         @NonNull UUID datasetId,
                                         @NonNull String name,
                                         @NonNull String dataType,
                                         @NonNull String metadata) {
        this.id = id;
        this.datasetId = datasetId;
        this.name = name;
        this.dataType = dataType;
        this.metadata = metadata;
    }

    @Override
    @SkipPatching
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(UUID datasetId) {
        this.datasetId = datasetId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Id
    @Column( name = "id", columnDefinition = "BINARY(16)" )
    @NonNull
    private UUID id;
    @NonNull
    @Column(columnDefinition = "BINARY(16)" )
    private UUID datasetId;
    @NonNull
    private String name;
    @NonNull
    private String dataType;
    @NonNull
    private String metadata;
}
