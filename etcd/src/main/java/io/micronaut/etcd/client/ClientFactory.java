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

import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import java.net.URI;
import java.util.Collection;
import javax.inject.Singleton;

/**
 * Client factory for jetcd clients.
 */
public class ClientFactory {

  /**
   * Create a singleton {@link KV} client, based on the string endpoints.
   *
   * @param endpoints
   * @return {@link KV}
   */
  public KV etcdKVClient(String endpoints) {
    Client client = Client.builder().endpoints(endpoints.split(",")).build();
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the URI endpoints.
   *
   * @param endpoints
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(URI endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the Collection of URI endpoints.
   *
   * @param endpoints
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(Collection<URI> endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

}
