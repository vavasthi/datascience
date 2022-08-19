package com.avasthi.datascience.pipeline.server.entities;

import com.avasthi.datascience.caching.annotations.SkipPatching;
import com.avasthi.datascience.caching.pojos.CachedPojo;
import com.avasthi.datascience.pipeline.common.pojos.InputSourceType;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name="input_sources")
@Table(name = "input_sources", uniqueConstraints = {
        @UniqueConstraint(name = "uq_name", columnNames = {"name"})
})
public class InputSourceEntity extends CachedPojo<UUID>  implements Serializable  {
    public InputSourceEntity() {
    }

    public InputSourceEntity(@NonNull UUID id, @NonNull String name, @NonNull String dbName, @NonNull InputSourceType type, @NonNull String username, @NonNull String password, @NonNull String hostname, @NonNull int portNumber) {
        this.id = id;
        this.name = name;
        this.dbName = dbName;
        this.type = type;
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.portNumber = portNumber;
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public InputSourceType getType() {
        return type;
    }

    public void setType(InputSourceType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    @Id
    @Column( name = "id", columnDefinition = "BINARY(16)" )
    @NonNull
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private String dbName;
    @NonNull
    private InputSourceType type;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String hostname;
    @NonNull
    private int portNumber;
}
