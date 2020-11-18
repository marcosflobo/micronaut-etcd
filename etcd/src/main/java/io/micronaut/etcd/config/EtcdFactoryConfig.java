package io.micronaut.etcd.config;

import io.micronaut.context.annotation.Parameter;

public abstract class EtcdFactoryConfig {
  private String endpoints;

  public EtcdFactoryConfig(@Parameter String endpoints) {
    this.endpoints = endpoints;
  }

  public String getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(String endpoints) {
    this.endpoints = endpoints;
  }
}
