package com.avasthi.datascience.pipeline.server.service;

import com.avasthi.datascience.pipeline.server.caching.InputSourceCachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InputSourceService {
    @Autowired
    private InputSourceCachingService inputSourceCachingService;

}
