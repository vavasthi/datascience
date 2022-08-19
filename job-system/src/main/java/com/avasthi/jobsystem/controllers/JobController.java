package com.avasthi.jobsystem.controllers;

import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.jobsystem.caching.JobsCachingService;
import com.avasthi.datascience.pipeline.common.pojos.JobResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping(Constants.Endpoints.V1.JOBS_ENDPOINT)
public class JobController {
    @Autowired
    private JobsCachingService jobsCachingService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Optional<JobResponse> getJobStatus(@PathVariable("id") UUID id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return Optional.of(jobsCachingService.getCurrentStatus(id));
    }
    @RequestMapping(value = "/{id}/logs", method = RequestMethod.POST)
    public @ResponseBody
    Optional<JobResponse> addJobLog(@PathVariable("id") UUID id, @RequestBody List<String> jobLogList) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        jobsCachingService.addLogs(id, jobLogList);
        return Optional.of(jobsCachingService.getCurrentStatus(id));
    }
/*    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public @ResponseBody
    Optional<JobResponse> updateJobStatus(@PathVariable("id") UUID id, JobStatus status) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return Optional.of(jobsCachingService.getCurrentStatus(id));
    }*/
}

