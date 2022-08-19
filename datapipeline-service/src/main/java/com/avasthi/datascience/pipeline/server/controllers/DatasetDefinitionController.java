package com.avasthi.datascience.pipeline.server.controllers;

import com.avasthi.datascience.caching.exceptions.EntityDoesnotExist;
import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinition;
import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinitionCreatePojo;
import com.avasthi.datascience.pipeline.common.pojos.DatasetType;
import com.avasthi.datascience.pipeline.common.pojos.JobContext;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.caching.DatasetDefinitionCachingService;
import com.avasthi.datascience.pipeline.server.caching.InputSourceCachingService;
import com.avasthi.datascience.pipeline.server.entities.DatasetDefinitionEntity;
import com.avasthi.datascience.pipeline.server.utils.ObjectConverter;
import com.avasthi.jobsystem.JobManager;
import com.avasthi.jobsystem.handlers.AbstractJobHandler;
import com.avasthi.jobsystem.handlers.SparkJobLauncherHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Controller
@RequestMapping(Constants.Endpoints.V1.DATASET_ENDPOINT)
public class DatasetDefinitionController {

    @Autowired
    private DatasetDefinitionCachingService datasetDefinitionCachingService;
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
    public @ResponseBody  Iterable<DatasetDefinition> getDataset() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return StreamSupport.stream(datasetDefinitionCachingService.findAll().spliterator(), false)
                .map(e -> converter.convert(e))
                .collect(Collectors.toList());
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<DatasetDefinition> getInputSource(@PathVariable("id") UUID id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Optional<DatasetDefinitionEntity> optionalDatasetEntity = datasetDefinitionCachingService.findById(id);
        if (optionalDatasetEntity.isPresent()) {
            DatasetDefinition dd = converter.convert(optionalDatasetEntity.get());
            return Optional.of(dd);
        }
        throw EntityDoesnotExist.builder()
                .errorCode(HttpStatus.EXPECTATION_FAILED.value())
                .message(String.format("Input source with id %s not found", id.toString()))
                .build();
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody  Optional<JobContext> createDataset(@RequestBody DatasetDefinitionCreatePojo ddc,
                                                             HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        AbstractJobHandler jobHandler = new SparkJobLauncherHandler(appResource, Constants.JOBS.VALIDATE_DATASET_QUERY, sparkMaster);
        if (ddc.getType() == DatasetType.DERIVED) {
            return createDerivedDataset(jobHandler, ddc, servletResponse);
        }
        else {
            return createPrimaryDataset(jobHandler, ddc, servletResponse);
        }
    }
    private Optional<JobContext> createPrimaryDataset(AbstractJobHandler jobHandler,
                                                      DatasetDefinitionCreatePojo ddc,
                                                      HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (!CollectionUtils.isEmpty(ddc.getDependentOn())) {

            return requestFailed(jobHandler, "A primary dataset cano not have dependenicies.", HttpStatus.FAILED_DEPENDENCY, servletResponse);
        }
        else {

            DatasetDefinitionEntity entity = new DatasetDefinitionEntity(
                    UUID.randomUUID(),
                    ddc.getName(),
                    ddc.getInputSourceId(),
                    ddc.getType(),
                    ddc.getTableName(),
                    ddc.getQuery(),
                    null
            );
            return execute(jobHandler, entity, servletResponse);
        }
    }
    private Optional<JobContext> createDerivedDataset(AbstractJobHandler jobHandler,
                                                      DatasetDefinitionCreatePojo ddc,
                                                      HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (CollectionUtils.isEmpty(ddc.getDependentOn())) {

            return requestFailed(jobHandler, "A derived dataset should have some dependenicies.", HttpStatus.FAILED_DEPENDENCY, servletResponse);
        }
        else {
            Set<DatasetDefinitionEntity> dependsOnSet = new HashSet<>();
            Set<UUID> nonExistentDependsOnSet = new HashSet<>();
            for (UUID did : ddc.getDependentOn()) {
                Optional<DatasetDefinitionEntity> optionalDatasetDefinitionEntity
                        = datasetDefinitionCachingService.findById(did);
                if (optionalDatasetDefinitionEntity.isPresent()) {
                    dependsOnSet.add(optionalDatasetDefinitionEntity.get());
                }
                else {
                    nonExistentDependsOnSet.add(did);
                }
            }
            if (!nonExistentDependsOnSet.isEmpty()) {

                return requestFailed(jobHandler,
                        String.format("Following dependencies were not met. %s.",
                        nonExistentDependsOnSet
                                .stream()
                                .map(e -> e.toString())
                                .collect(Collectors.joining(","))), HttpStatus.FAILED_DEPENDENCY, servletResponse);
            }
            else {

                DatasetDefinitionEntity entity = new DatasetDefinitionEntity(
                        UUID.randomUUID(),
                        ddc.getName(),
                        ddc.getInputSourceId(),
                        ddc.getType(),
                        ddc.getTableName(),
                        ddc.getQuery(),
                        dependsOnSet
                );
                return execute(jobHandler, entity, servletResponse);
            }
        }
    }
    private Optional<JobContext> requestFailed(AbstractJobHandler jobHandler, String message, HttpStatus status, HttpServletResponse servletResponse) {

        JobContext jobContext = jobManager.executeFailed(jobHandler, UUID.randomUUID(), message, status);
        servletResponse.setStatus(status.value());
        return Optional.of(jobContext);
    }
    private Optional<JobContext> execute(AbstractJobHandler jobHandler, DatasetDefinitionEntity entity, HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        Optional<DatasetDefinitionEntity> optionalDatasetEntity = datasetDefinitionCachingService.create(entity);
        JobContext jobContext = jobManager.execute(jobHandler, entity.getId());
        servletResponse.setStatus(HttpStatus.ACCEPTED.value());
        return Optional.of(jobContext);
    }
    private Map<UUID, Set<UUID>> createUUIDMap(Map<UUID, Set<UUID>> dependencyMap, DatasetDefinitionEntity entity) {
        if (!CollectionUtils.isEmpty(entity.getDependentOn())) {
            dependencyMap.put(entity.getId(), entity.getDependentOn().stream().map(e -> e.getId()).collect(Collectors.toSet()));
            for (DatasetDefinitionEntity newdde : entity.getDependentOn()) {
                createUUIDMap(dependencyMap, newdde);
            }
        }
        else {
            dependencyMap.put(entity.getId(), new HashSet<>());
        }
        return dependencyMap;
    }
}
