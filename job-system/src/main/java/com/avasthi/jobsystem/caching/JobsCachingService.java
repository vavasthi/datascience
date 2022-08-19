
package com.avasthi.jobsystem.caching;

import com.avasthi.datascience.caching.annotations.DefineCache;
import com.avasthi.datascience.caching.pojos.KeyPrefixForCache;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.jobsystem.configurations.JobsCacheConfig;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import com.avasthi.datascience.pipeline.common.pojos.JobLog;
import com.avasthi.datascience.pipeline.common.pojos.JobResponse;
import com.avasthi.datascience.pipeline.common.pojos.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class is a spring service that manages cache infrastructure for jobs system. The jobs system only facilitates
 * offlining a function call. The purpose of this infrastructure is to provide a mechanism to check the status of a
 * long running task.
 */
@Service
@DefineCache(name = Constants.CACHES.JOBS_CACHING_NAME,
        prefix = Constants.CACHES.JOBS_CACHING_PREFIX,
        expiry = Constants.Times.ONE_DAY)
public class JobsCachingService {

    private final String logString = "LOG";
    @Autowired
    private JobsCacheConfig redisConfiguration;

    private long getExpiry() {

        return this.getClass().getAnnotation(DefineCache.class).expiry();
    }

    protected String getPrefix() {

        return this.getClass().getAnnotation(DefineCache.class).prefix();
    }
    public JobContext createEntityValidationJob(UUID entityId) {
        JobContext context = JobContext.builder()
                .id(UUID.randomUUID())
                .entityId(entityId)
                .startTime(new Date())
                .lastUpdateTime(new Date())
                .status(JobStatus.ONGOING)
                .build();
        redisConfiguration
                .redisTemplate()
                .opsForValue()
                .set(new KeyPrefixForCache(getPrefix(), context.getId()), context, getExpiry(), TimeUnit.SECONDS);
        return context;
    }
    public JobContext updateJob(JobContext context) {

        redisConfiguration
                .redisTemplate()
                .opsForValue()
                .set(new KeyPrefixForCache(getPrefix(), context.getId()), context, getExpiry(), TimeUnit.SECONDS);
        return context;
    }
    public void addLog(UUID id, String message) {
        KeyPrefixForCache kpf = getLogKey(id);
        redisConfiguration.redisTemplate().opsForList().leftPush(kpf,
                JobLog.builder()
                        .message(message)
                        .timestamp(new Date())
                        .build());
        redisConfiguration.redisTemplate().expire(kpf, getExpiry(), TimeUnit.SECONDS);
    }
    public void addLogs(UUID id, List<String> messages) {
        for (String m: messages) {
            addLog(id, m);
        }
    }
    private void delete(UUID id) {
        redisConfiguration.redisTemplate().delete(Arrays.asList(getObjectKey(id), getLogKey(id)));
    }
    public JobResponse getCurrentStatus(UUID id) {
        JobContext context
                = JobContext.class.cast(redisConfiguration.redisTemplate().opsForValue().get(getObjectKey(id)));
        List<JobLog> jobLogList
                = redisConfiguration
                .redisTemplate()
                .opsForList()
                .range(getLogKey(id), 0, -1)
                .stream()
                .map(e -> JobLog.class.cast(e))
                .collect(Collectors.toList());
        return JobResponse.builder()
                .logs(jobLogList)
                .context(context)
                .build();
    }

    private KeyPrefixForCache getLogKey(UUID id) {
        return new KeyPrefixForCache(String.format("%s/%s", getPrefix(), logString), id);
    }
    private KeyPrefixForCache getObjectKey(UUID id) {

        return new KeyPrefixForCache(getPrefix(), id);
    }

    public void updateStatus(JobContext context, JobStatus status) {
        context = JobContext.class.cast(redisConfiguration
                .redisTemplate()
                .opsForValue()
                .get(new KeyPrefixForCache(getPrefix(), context.getId())));
        context.setStatus(status);
        updateJob(context);
    }
}