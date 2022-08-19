package com.avasthi.jobsystem.pojos;

import java.io.Serializable;

public class SchedulerBasicReturnValue implements Serializable {

    public SchedulerBasicReturnValue(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int status;
    private String message;
}
