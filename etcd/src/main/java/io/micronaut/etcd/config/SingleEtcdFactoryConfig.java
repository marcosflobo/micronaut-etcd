package io.micronaut.etcd.config;


import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("etcd")
public class SingleEtcdFactoryConfig extends EtcdFactoryConfig{

  public static final String DEFAULT_ENDPOINTS = "http://localhost:2379";

  public SingleEtcdFactoryConfig() {
    super(DEFAULT_ENDPOINTS);
  }
}
