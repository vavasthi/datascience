package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class JobContext implements Serializable {

    private UUID id;
    private UUID entityId;
    private JobStatus status;
    private Date startTime;
    private Date lastUpdateTime;
    private Date endTime;

}
