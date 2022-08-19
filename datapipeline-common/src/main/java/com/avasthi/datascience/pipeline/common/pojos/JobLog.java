package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class JobLog implements Serializable {

    private Date timestamp;
    private String message;
}
