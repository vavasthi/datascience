package com.avasthi.datascience.caching.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class NoMatchingkeyCombinationsExist extends BaseRestException {
    @Builder
    public NoMatchingkeyCombinationsExist(HttpStatus httpStatus, String message, Throwable cause) {
        super(httpStatus, message, cause);
    }

}
