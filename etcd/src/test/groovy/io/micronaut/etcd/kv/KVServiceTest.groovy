package io.micronaut.etcd.kv

import io.etcd.jetcd.ByteSequence
import io.micronaut.etcd.config.EtcdFactoryConfig
import io.micronaut.etcd.config.SingleEtcdFactoryConfig
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import spock.lang.Shared
import spock.lang.Specification

class KVServiceTest extends Specification {

    @Shared
    GenericContainer etcdContainer =
            new GenericContainer("quay.io/coreos/etcd:v3.4.9")
                    .withExposedPorts(2379, 4001)
                    .withCommand("/usr/local/bin/etcd -advertise-client-urls http://0.0.0.0:2379 -listen-client-urls http://0.0.0.0:2379")
                    .waitingFor(new LogMessageWaitStrategy().withRegEx("(?s).*ready to serve client requests.*"))

    def "Get service works with empty storage"() {
        given:
        etcdContainer.start()

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(2379)}")
        KVService kvService = new KVService(config)
        ByteSequence expected = null

        when:
        ByteSequence ret = kvService.get("foo")

        then:
        expected ==  ret

        cleanup:
        etcdContainer.stop()
    }
}
