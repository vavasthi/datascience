package com.avasthi.datascience.caching.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseRestException extends ResponseStatusException {

    public BaseRestException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
