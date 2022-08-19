package com.avasthi.datascience.pipeline.common.pojos;


public enum InputSourceType {
    INTERNAL("internal"),
    MYSQL("mysql"),
    POSTGRESQL("postgresql");

    private final String connType;

    public String getConnType() {
        return connType;
    }
    InputSourceType(String connType) {
        this.connType = connType;
    }
}
