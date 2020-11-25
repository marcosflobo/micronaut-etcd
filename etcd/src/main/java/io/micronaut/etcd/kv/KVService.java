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

import com.google.protobuf.ByteString;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.micronaut.etcd.client.ClientFactory;
import io.micronaut.etcd.config.EtcdFactoryConfig;
import io.micronaut.etcd.util.ExternalByteSequence;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service to work with the key/value api of etcd.
 */
public class KVService {

  private final KV kvClient;

  public KVService(EtcdFactoryConfig config) {
    this.kvClient = new ClientFactory().etcdKVClient(config);
  }

  /**
   * Gets information from etcd based on the key provided.
   * @param key The key used to get a value
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public ByteSequence get (ByteSequence key) throws ExecutionException,
      InterruptedException {
    return get(key, GetOption.DEFAULT);
  }

  public byte[] get (String key)
      throws ExecutionException, InterruptedException {
    ByteSequence ret = get(ByteSequence.from(ByteString.copyFrom(key.getBytes())));
    if (ret == null) {
      return null;
    }
    return ret.getBytes();
//    ByteString b = ByteString.copyFrom(a);
//    return new ExternalByteSequence(b);
  }

  public ExternalByteSequence get (ExternalByteSequence key)
      throws ExecutionException, InterruptedException {
    ByteSequence aux = ByteSequence.from(key.getBytes());
    ByteSequence ret = get(aux);
    return new ExternalByteSequence(ByteString.copyFrom(ret.getBytes()));
  }

  /**
   * Gets information from etcd based on the key provided.
   * @param key The key used to get a value
   * @param getOption Options for GET action
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public ByteSequence get (ByteSequence key, GetOption getOption) throws ExecutionException,
      InterruptedException {
    CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(key, getOption);
    GetResponse response = getResponseCompletableFuture.get();
    if (response.getKvs().isEmpty()) {
      return null;
    }
    return response.getKvs().get(0).getValue();
  }

  /**
   *
   * @param key The key used to store the value
   * @param value The value to store
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public ByteSequence put (ByteSequence key, ByteSequence value)
      throws ExecutionException, InterruptedException {
    return put(key, value, PutOption.DEFAULT);
  }

  /**
   *
   * @param key The key used to store the value
   * @param value The value to store
   * @param putOption The options used to put the key/value
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public ByteSequence put (ByteSequence key, ByteSequence value, PutOption putOption)
      throws ExecutionException, InterruptedException {
    CompletableFuture<PutResponse> completableFuture = kvClient.put(key, value, putOption);
    return getValueFromCompletableFuturePutResponse(completableFuture);
  }

  /**
   *
   * @param completableFuture The object handle the PUT response
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  private ByteSequence getValueFromCompletableFuturePutResponse (CompletableFuture<PutResponse> completableFuture)
      throws ExecutionException, InterruptedException {
    PutResponse response = completableFuture.get();
    if (!response.hasPrevKv()) {
      return null;
    }
    return response.getPrevKv().getValue();
  }

}
