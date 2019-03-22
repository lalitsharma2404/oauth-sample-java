package com.example.oauthsample.db.domain;

import java.util.UUID;

public class IdentityModel {

  protected String uuid = UUID.randomUUID().toString();
  private String id;
  private Boolean deleted = Boolean.FALSE;
  private long createdDate;
  private long updatedDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(long createdDate) {
    this.createdDate = createdDate;
  }

  public long getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(long updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
