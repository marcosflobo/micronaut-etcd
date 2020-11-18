package io.micronaut.etcd.kv;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.micronaut.etcd.client.ClientFactory;
import io.micronaut.etcd.config.EtcdFactoryConfig;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class KVService {

  private final KV kvClient;

  public KVService(EtcdFactoryConfig config) {
    this.kvClient = new ClientFactory().etcdKVClient(config.getEndpoints());
  }

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
