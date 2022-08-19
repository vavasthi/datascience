package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class InputSourceCreatePojo implements Serializable  {

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
