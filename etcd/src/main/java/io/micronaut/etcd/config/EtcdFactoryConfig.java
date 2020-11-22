/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.etcd.config;

import io.micronaut.context.annotation.Parameter;

/**
 * Etcd configuration.
 */
public abstract class EtcdFactoryConfig {
  private String[] endpoints;

  /**
   * Constructor.
   * @param endpoints etcd server endpoints, at least one
   */
  public EtcdFactoryConfig(@Parameter String... endpoints) {
    this.endpoints = endpoints;
  }

  /**
   * Returns the comma-separated endpoints of the etcd cluster.
   * @return {@link String}
   */
  public String[] getEndpoints() {
    return endpoints;
  }

  /**
   * Sets the comma-separated endpoints of the etcd cluster.
   * @param endpoints etcd server endpoints, at least one
   */
  public void setEndpoints(String[] endpoints) {
    this.endpoints = endpoints;
  }
}
