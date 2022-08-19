package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InputSource {
    private UUID id;
    private String name;
    private String dbName;
    private InputSourceType type;
    private String username;
    private String password;
    private String hostname;
    private int portNumber;
}
