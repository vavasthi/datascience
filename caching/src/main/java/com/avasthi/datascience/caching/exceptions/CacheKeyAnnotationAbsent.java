package com.avasthi.datascience.caching.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class CacheKeyAnnotationAbsent extends BaseRestException {
    @Builder
    public CacheKeyAnnotationAbsent(HttpStatus httpStatus, String message, Throwable cause) {
        super(httpStatus, message, cause);
    }
}
