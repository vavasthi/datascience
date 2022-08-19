package com.avasthi.jobsystem.pojos;

import java.io.Serializable;
import java.util.Date;

public class Target implements Serializable  {


  public enum STATUS {
    CREATED,
    COMPLETED,
    WAITING_FOR_RETRY,
    FAILED
  }

  public Target() {
    status = STATUS.CREATED;
    createdAt = new Date();
    count = 0;
  }
  public Target(STATUS status) {
    this.status = status;
  }

  public STATUS getStatus() {
    return status;
  }

  public void setStatus(STATUS status) {
    this.status = status;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(Date completedAt) {
    this.completedAt = completedAt;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void markFailed() {
    status = STATUS.FAILED;
    completedAt = new Date();
  }
  public void markCompleted() {
    status = STATUS.COMPLETED;
    completedAt = new Date();
  }
  public void markRetrying() {
    status = STATUS.WAITING_FOR_RETRY;
  }

  public boolean done() {
    return status == STATUS.COMPLETED || status == STATUS.FAILED;
  }
  private STATUS status;
  private int count;
  private Date createdAt;
  private Date completedAt;
}
