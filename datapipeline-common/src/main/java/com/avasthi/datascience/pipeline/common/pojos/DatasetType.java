package com.avasthi.datascience.pipeline.common.pojos;

public enum DatasetType {
    PRIMARY("PRIMARY"),
    DERIVED("DERIVED");

    private String desc;
    DatasetType(String desc) {
        this.desc = desc;
    }
}
