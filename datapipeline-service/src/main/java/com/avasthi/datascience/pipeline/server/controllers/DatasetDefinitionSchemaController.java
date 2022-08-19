package com.avasthi.datascience.pipeline.server.controllers;

import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinitionSchema;
import com.avasthi.datascience.pipeline.common.utils.Constants;
import com.avasthi.datascience.pipeline.server.caching.DatasetDefinitionSchemaCachingService;
import com.avasthi.datascience.pipeline.common.pojos.DatasetDefinitionSchemaCreatePojo;
import com.avasthi.datascience.pipeline.server.utils.ObjectConverter;
import com.avasthi.jobsystem.JobManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Controller
@RequestMapping(Constants.Endpoints.V1.DATASET_SCHEMA_ENDPOINT)
public class DatasetDefinitionSchemaController {

    @Autowired
    private DatasetDefinitionSchemaCachingService datasetDefinitionSchemaCachingService;
    @Autowired
    private JobManager jobManager;
    @Autowired
    private ObjectConverter converter;

    @Value("${spark.master}")
    private String sparkMaster;
   @Value("${spark.app.resource}")
   private String appResource;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody  Iterable<DatasetDefinitionSchema> getDataset(@PathVariable("datasetId") UUID datasetId) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return StreamSupport.stream(datasetDefinitionSchemaCachingService.findAll(datasetId).spliterator(), false)
                .map(e -> converter.convert(e))
                .collect(Collectors.toList());
    }
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody  List<DatasetDefinitionSchema> createDataset(@RequestBody List<DatasetDefinitionSchemaCreatePojo> ddcl,
                                                                      HttpServletResponse servletResponse)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        return StreamSupport.stream(datasetDefinitionSchemaCachingService.create(ddcl).spliterator(), false)
                .map(e -> converter.convert(e))
                .collect(Collectors.toList());
    }
}
