package com.avasthi.jobsystem;

import com.avasthi.jobsystem.caching.JobsCachingService;
import com.avasthi.jobsystem.handlers.AbstractJobHandler;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import com.avasthi.datascience.pipeline.common.pojos.JobStatus;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class JobManager {
    @Value("${datapipeline.application.basurl}")
    private String baseUrl;
    @Value(("${spark.master}"))
    private String sparkMaster;
    @Value(("${spark.executor.memory:512M}"))
    private String executorMemory;
    @Value(("${spark.driver.memory:1G}"))
    private String driverMemory;


    @Autowired
    private JobsCachingService jobsCachingService;

    private final int noOfExecutorThreads = 5;
    private final ExecutorService executorService = Executors.newFixedThreadPool(noOfExecutorThreads);
    public JobContext execute(AbstractJobHandler handler, UUID entityId) {

        JobContext context = jobsCachingService.createEntityValidationJob(entityId);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                log.log(Level.INFO, String.format("Scheduling a job with id %s", context.getId()));
                JobStatus status = handler.handle(jobsCachingService, context, sparkMaster, baseUrl, context.getId().toString(), driverMemory, executorMemory);
                jobsCachingService.updateStatus(context, status);
                log.log(Level.INFO, String.format("Completing job with id %s with status %s", context.getId(), status));
            }
        });
        return context;
    }
    public JobContext executeFailed(AbstractJobHandler handler, UUID entityId, String message, HttpStatus status) {

        JobContext context = jobsCachingService.createEntityValidationJob(entityId);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                log.log(Level.INFO, String.format("Scheduling a job with id %s", context.getId()));
                jobsCachingService.addLog(context.getId(), String.format("Scheduling a job with id %s", context.getId()));
                log.log(Level.INFO, message);
                jobsCachingService.addLog(context.getId(), message);
                log.log(Level.INFO, String.format("HTTP Status code is %s", status.toString()));
                jobsCachingService.addLog(context.getId(), String.format("HTTP Status code is %s", status.toString()));
                JobStatus status = handler.handle(jobsCachingService, context, sparkMaster, baseUrl, context.getId().toString(), driverMemory, executorMemory);
                jobsCachingService.updateStatus(context, JobStatus.FAILURE);
                log.log(Level.INFO, String.format("Completing job with id %s with status %s", context.getId(), status));
            }
        });
        return context;
    }
}
