package com.avasthi.datascience.caching.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public class EntityDoesnotExist extends BaseRestException {
    @Builder
    public EntityDoesnotExist(String message, Throwable cause, int errorCode, String id) {
        super(HttpStatus.NOT_FOUND, message, cause);
        this.id = id;
    }

    private String id;
}
