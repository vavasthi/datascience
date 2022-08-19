package com.avasthi.jobsystem.handlers;

import com.avasthi.jobsystem.caching.JobsCachingService;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import com.avasthi.datascience.pipeline.common.pojos.JobStatus;

public interface AbstractJobHandler {
    JobStatus handle(JobsCachingService cachingService, JobContext context, String... appArgs);
}
