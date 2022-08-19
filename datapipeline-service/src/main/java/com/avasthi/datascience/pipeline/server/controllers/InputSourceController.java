package com.avasthi.datascience.pipeline.server.controllers;

import com.avasthi.datascience.caching.exceptions.EntityDoesnotExist;
import com.avasthi.datascience.pipeline.common.pojos.InputSource;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.caching.InputSourceCachingService;
import com.avasthi.datascience.pipeline.server.entities.InputSourceEntity;
import com.avasthi.datascience.pipeline.common.pojos.InputSourceCreatePojo;
import com.avasthi.datascience.pipeline.server.utils.ObjectConverter;
import com.avasthi.jobsystem.JobManager;
import com.avasthi.jobsystem.handlers.AbstractJobHandler;
import com.avasthi.jobsystem.handlers.SparkJobLauncherHandler;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Controller
@RequestMapping(Constants.Endpoints.V1.INPUT_SOURCE_ENDPOINT)
public class InputSourceController {

    @Autowired
    private InputSourceCachingService inputSourceCachingService;
    @Autowired
    private JobManager jobManager;
    @Autowired
    private ObjectConverter converter;

    @Value("${spark.master}")
    private String sparkMaster;
   @Value("${spark.app.resource}")
   private String appResource;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody  Iterable<InputSource> getInputSource() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return StreamSupport.stream(inputSourceCachingService.findAll().spliterator(), false)
                .map(e -> converter.convert(e))
                .collect(Collectors.toList());
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<InputSource> getInputSource(@PathVariable("id") UUID id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Optional<InputSourceEntity> optionalInputSource = inputSourceCachingService.findById(id);
        if (optionalInputSource.isPresent()) {
            return Optional.of(converter.convert(optionalInputSource.get()));
        }
        throw EntityDoesnotExist.builder()
                .errorCode(HttpStatus.EXPECTATION_FAILED.value())
                .message(String.format("Input source with id %s not found", id.toString()))
                .build();
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody  Optional<JobContext> createInputSource(@RequestBody InputSourceCreatePojo isc,
                                                                 HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        InputSourceEntity entity = new InputSourceEntity(
                UUID.randomUUID(),
                isc.getName(),
                isc.getDbName(),
                isc.getType(),
                isc.getUsername(),
                isc.getPassword(),
                isc.getHostname(),
                isc.getPortNumber()
        );
        Optional<InputSourceEntity> optionalInputSource = inputSourceCachingService.create(entity);
        AbstractJobHandler jobHandler = new SparkJobLauncherHandler(appResource, Constants.JOBS.VALIDATE_DB_CONNECTION, sparkMaster);
        JobContext jobContext = jobManager.execute(jobHandler, entity.getId());
        servletResponse.setStatus(HttpStatus.ACCEPTED.value());
        return Optional.of(jobContext);
    }
}
