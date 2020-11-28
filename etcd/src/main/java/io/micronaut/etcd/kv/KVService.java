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
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.micronaut.etcd.client.ClientFactory;
import io.micronaut.etcd.config.EtcdFactoryConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service to work with the key/value api of etcd.
 */
public class KVService {

  /**
   * The KV Client from jetcd.
   */
  private final KV kvClient;

  /**
   * Constructor.
   * @param config configuration to connect to the etcd server.
   */
  public KVService(EtcdFactoryConfig config) {
    this.kvClient = new ClientFactory().etcdKVClient(config);
  }

  /**
   * Get a value from a key.
   * @param key Key to search for
   * @param getOption {@link GetOption} for the GET request
   * @return The value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] get (byte[] key, GetOption getOption)
      throws ExecutionException, InterruptedException {
    ByteSequence ret = get(ByteSequence.from(ByteString.copyFrom(key)), getOption);
    if (ret == null) {
      return null;
    }
    return ret.getBytes();
  }

  /**
   * Get a value from a key.
   * @param key Key to search for
   * @return The value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] get (byte[] key)
      throws ExecutionException, InterruptedException {
    return get(key, GetOption.DEFAULT);
  }

  /**
   * Get a value from a key.
   * @param key Key to search for
   * @param getOption {@link GetOption} for the GET request
   * @return The value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] get (String key, GetOption getOption)
      throws ExecutionException, InterruptedException {
    return get(key.getBytes(), getOption);
  }

  /**
   * Get a value from a key.
   * @param key Key to search for
   * @return The value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] get (String key)
      throws ExecutionException, InterruptedException {
    return get(key.getBytes(), GetOption.DEFAULT);
  }

  /**
   * Gets information from etcd based on the key provided.
   * @param key The key used to get a value
   * @param getOption Options for GET action
   * @return {@link ByteSequence}
   * @throws ExecutionException
   * @throws InterruptedException
   */
  protected ByteSequence get (ByteSequence key, GetOption getOption) throws ExecutionException,
      InterruptedException {
    CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(key, getOption);
    GetResponse response = getResponseCompletableFuture.get();
    if (response.getKvs().isEmpty()) {
      return null;
    }
    return response.getKvs().get(0).getValue();
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (byte[] key, byte[] value)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key)),
        ByteSequence.from(ByteString.copyFrom(value)), PutOption.DEFAULT);
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @param putOption {@link PutOption} The options used to put the key/value
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (byte[] key, byte[] value, PutOption putOption)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key)),
        ByteSequence.from(ByteString.copyFrom(value)), putOption);
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (String key, byte[] value)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key.getBytes())),
        ByteSequence.from(ByteString.copyFrom(value)), PutOption.DEFAULT);
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @param putOption {@link PutOption} The options used to put the key/value
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (String key, byte[] value, PutOption putOption)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key.getBytes())),
        ByteSequence.from(ByteString.copyFrom(value)), putOption);
  }

  /**
   *
   * @param key The key used to store the value
   * @param value The value to store
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (String key, String value)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key.getBytes())),
        ByteSequence.from(ByteString.copyFrom(value.getBytes())), PutOption.DEFAULT);
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @param putOption {@link PutOption} The options used to put the key/value
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public byte[] put (String key, String value, PutOption putOption)
      throws ExecutionException, InterruptedException {
    return put(ByteSequence.from(ByteString.copyFrom(key.getBytes())),
        ByteSequence.from(ByteString.copyFrom(value.getBytes())), putOption);
  }

  /**
   * Inserts key-value.
   * @param key The key used to store the value
   * @param value The value to store
   * @param putOption {@link PutOption} The options used to put the key/value
   * @return The previous key-pair value as byte array
   * @throws ExecutionException
   * @throws InterruptedException
   */
  protected byte[] put (ByteSequence key, ByteSequence value, PutOption putOption)
      throws ExecutionException, InterruptedException {
    CompletableFuture<PutResponse> completableFuture = kvClient.put(key, value, putOption);
    ByteSequence byteSequence = getValueFromCompletableFuturePutResponse(completableFuture);
    if (byteSequence == null) {
      return null;
    }
    return byteSequence.getBytes();
  }

  /**
   * Deletes a key-value.
   * @param key The key to be deleted on etcd cluster
   * @return Number of key-value elements deleted
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public long delete (String key) throws ExecutionException, InterruptedException {
    return delete(ByteSequence.from(ByteString.copyFrom(key.getBytes())), DeleteOption.DEFAULT);
  }

  /**
   * Deletes a key-value with the possibility to provide deletion options.
   * @param key The key to be deleted on etcd cluster
   * @param deleteOption {@link DeleteOption} Deletion options
   * @return Number of key-value elements deleted
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public long delete (String key, DeleteOption deleteOption) throws ExecutionException, InterruptedException {
    return delete(ByteSequence.from(ByteString.copyFrom(key.getBytes())), deleteOption);
  }

  /**
   * Deletes a key-value with the possibility to provide deletion options.
   * @param key The key to be deleted on etcd cluster
   * @param deleteOption {@link DeleteOption} Deletion options
   * @return Number of key-value elements deleted
   * @throws ExecutionException
   * @throws InterruptedException
   */
  protected long delete (ByteSequence key, DeleteOption deleteOption)
      throws ExecutionException, InterruptedException {
    CompletableFuture<DeleteResponse> completableFuture = kvClient.delete(key, deleteOption);
    DeleteResponse deleteResponse = completableFuture.get();
    return deleteResponse.getDeleted();
  }

  /**
   *
   * @param completableFuture The object handle the PUT response
   * @return {@link ByteSequence} Of the previous key-pair value
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
