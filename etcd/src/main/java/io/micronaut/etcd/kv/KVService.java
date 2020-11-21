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
package io.micronaut.etcd.kv;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.micronaut.etcd.client.ClientFactory;
import io.micronaut.etcd.config.EtcdFactoryConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service to work with the key/value api of etcd.
 */
public class KVService {

  private final KV kvClient;

  public KVService(EtcdFactoryConfig config) {
    this.kvClient = new ClientFactory().etcdKVClient(config.getEndpoints());
  }

  /**
   * Gets information from etcd based on the key provided.
   * @param key
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public ByteSequence get (String key) throws ExecutionException, InterruptedException {
    ByteSequence keyByteSequence = ByteSequence.from(key.getBytes());
    CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(keyByteSequence);
    GetResponse response = getResponseCompletableFuture.get();
    if (response.getKvs().isEmpty()) {
      return null;
    }
    return response.getKvs().get(0).getValue();
  }

}
