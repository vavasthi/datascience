package com.avasthi.datascience.pipeline.server.entities;

import com.avasthi.datascience.caching.annotations.SkipPatching;
import com.avasthi.datascience.caching.pojos.CachedPojo;
import com.avasthi.datascience.pipeline.common.pojos.DatasetType;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity(name="datasets")
@Table(name = "datasets", uniqueConstraints = {
        @UniqueConstraint(name = "uq_name", columnNames = {"name"})
})
public class DatasetDefinitionEntity extends CachedPojo<UUID>  implements Serializable  {
    public DatasetDefinitionEntity() {
    }

    public DatasetDefinitionEntity(@NonNull UUID id, @NonNull String name, @NonNull UUID inputSourceId, @NonNull DatasetType type, @NonNull String tableName, @NonNull String query, Set<DatasetDefinitionEntity> dependentOn) {
        this.id = id;
        this.name = name;
        this.inputSourceId = inputSourceId;
        this.type = type;
        this.tableName = tableName;
        this.query = query;
        this.dependentOn = dependentOn;
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

    public UUID getInputSourceId() {
        return inputSourceId;
    }

    public void setInputSourceId(UUID inputSourceId) {
        this.inputSourceId = inputSourceId;
    }

    public DatasetType getType() {
        return type;
    }

    public void setType(DatasetType type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Set<DatasetDefinitionEntity> getDependentOn() {
        return dependentOn;
    }

    public void setDependentOn(Set<DatasetDefinitionEntity> dependentOn) {
        this.dependentOn = dependentOn;
    }

    @Id
    @Column( name = "id", columnDefinition = "BINARY(16)" )
    @NonNull
    private UUID id;
    @NonNull
    private String name;
    @Column( name = "inputSourceId", columnDefinition = "BINARY(16)" )
    private UUID inputSourceId;
    @NonNull
    private DatasetType type;
    @NonNull
    private String tableName;
    @NonNull
    @Lob
    private String query;
    @JoinTable(name = "depends_on_dataset",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dependentOnId", referencedColumnName = "id"))
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<DatasetDefinitionEntity> dependentOn;
}
