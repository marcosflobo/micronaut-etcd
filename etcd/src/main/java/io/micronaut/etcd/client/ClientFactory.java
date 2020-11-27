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
package io.micronaut.etcd.client;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.KV;
import io.micronaut.etcd.config.EtcdFactoryConfig;
import java.net.URI;
import java.util.Collection;
import javax.inject.Singleton;

/**
 * Client factory for jetcd clients.
 */
@Singleton
public class ClientFactory {

  /**
   * Create a KV client {@link KV} base on the configuration injected.
   * @param config The configuration to connect to the etcd server
   * @return {@link KV}
   */
  public KV etcdKVClient(EtcdFactoryConfig config) {
    Client client = getClient(config);
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the string endpoints.
   *
   * @param endpoints Array of String endpoints
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(String... endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the URI endpoints.
   *
   * @param endpoints Array of URI endpoints
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(URI... endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the Collection of URI endpoints.
   *
   * @param endpoints Collection of URI endpoints
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(Collection<URI> endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

  /**
   * Gets a generic etcd client factory to obtain any of the available etcd clients.
   * @param config The configuration to connect to the etcd server
   * @return {@link Client}
   */
  private Client getClient(EtcdFactoryConfig config) {
    ClientBuilder clientBuilder = Client.builder().endpoints(config.getEndpoints());
    if (config.getUser() != null) {
      clientBuilder.user(ByteSequence.from(config.getUser().getBytes()));
    }
    if (config.getPassword() != null) {
      clientBuilder.password(ByteSequence.from(config.getPassword().getBytes()));
    }
    if (config.getExecutorService() != null) {
      clientBuilder.executorService(config.getExecutorService());
    }
    if (config.getLoadBalancerPolicy() != null) {
      clientBuilder.loadBalancerPolicy(config.getLoadBalancerPolicy());
    }
    if (config.getSslContext() != null) {
      clientBuilder.sslContext(config.getSslContext());
    }
    if (config.getAuthority() != null) {
      clientBuilder.authority(config.getAuthority());
    }
    if (config.getMaxInboundMessageSize() != null) {
      clientBuilder.maxInboundMessageSize(config.getMaxInboundMessageSize());
    }
    if (config.getHeaders() != null) {
      clientBuilder.headers(config.getHeaders());
    }
    if (config.getInterceptors() != null) {
      clientBuilder.interceptors(config.getInterceptors());
    }
    clientBuilder.namespace(config.getNamespace());
    clientBuilder.retryDelay(config.getRetryDelay());
    clientBuilder.retryMaxDelay(config.getRetryMaxDelay());
    clientBuilder.keepaliveTimeMs(config.getKeepaliveTimeMs());
    clientBuilder.keepaliveTimeoutMs(config.getKeepaliveTimeoutMs());
    clientBuilder.keepaliveWithoutCalls(config.getKeepaliveWithoutCalls());
    clientBuilder.retryChronoUnit(config.getRetryChronoUnit());
    if (!config.getRetryMaxDuration().isEmpty()) {
      clientBuilder.retryMaxDuration(config.getRetryMaxDuration());
    }
    if (config.getConnectTimeoutMs() != null) {
      clientBuilder.connectTimeoutMs(config.getConnectTimeoutMs());
    }
    clientBuilder.discovery(config.isDiscovery());
    return clientBuilder.build();
  }

}
