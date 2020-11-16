package io.micronaut.etcd.client;

import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.micronaut.context.annotation.Factory;
import java.net.URI;
import java.util.Collection;
import javax.inject.Singleton;

@Factory
public class ClientFactory {

  /**
   * Create a singleton {@link KV} client, based on the string endpoints.
   *
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(String endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

  /**
   * Create a singleton {@link KV} client, based on the URI endpoints.
   *
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
   * @return {@link KV}
   */
  @Singleton
  public KV etcdKVClient(Collection<URI> endpoints) {
    Client client = Client.builder().endpoints(endpoints).build();
    return client.getKVClient();
  }

}
