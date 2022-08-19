package com.avasthi.datascience.pipeline.common.pojos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobResponse {
    private JobContext context;
    private List<JobLog> logs;
}
