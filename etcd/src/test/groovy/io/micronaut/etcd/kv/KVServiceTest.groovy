package io.micronaut.etcd.kv

import com.google.protobuf.ByteString
import io.etcd.jetcd.ByteSequence
import io.micronaut.etcd.config.EtcdFactoryConfig
import io.micronaut.etcd.config.SingleEtcdFactoryConfig
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import spock.lang.Shared
import spock.lang.Specification

import static com.google.common.base.Charsets.UTF_8;


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
        String key = "foo"

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(2379)}")
        KVService kvService = new KVService(config)
        ByteSequence expected = null

        when:
        ByteSequence ret = kvService.get(ByteSequence.from(key, UTF_8))

        then:
        expected ==  ret

        cleanup:
        etcdContainer.stop()
    }

    def "Put single integer" () {
        given:
        etcdContainer.start()
        String key = "foo"
        Integer value = new Integer(69)

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(2379)}")
        KVService kvService = new KVService(config)
        ByteSequence expectedFromPut = null

        when:
        ByteSequence retFromPut = kvService.put(ByteSequence.from(key.getBytes()),
                ByteSequence.from(BigInteger.valueOf(value).toByteArray()))
        ByteSequence ret = kvService.get(ByteSequence.from(key, UTF_8))

        then:
        expectedFromPut ==  retFromPut
        BigInteger.valueOf(value).toByteArray() == ((ByteString)ret.getAt("byteString")).toByteArray()

        cleanup:
        etcdContainer.stop()
    }

    def "Put single string" () {
        given:
        etcdContainer.start()
        String key = "foo"
        String value = "bar"

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(2379)}")
        KVService kvService = new KVService(config)
        ByteSequence expectedFromPut = null

        when:
        ByteSequence retFromPut = kvService.put(ByteSequence.from(key, UTF_8),
                ByteSequence.from(value, UTF_8))
        ByteSequence ret = kvService.get(ByteSequence.from(key, UTF_8))

        then:
        expectedFromPut ==  retFromPut
        value.getBytes() == ret.getBytes()

        cleanup:
        etcdContainer.stop()
    }

    def "Put several String values" () {
        given:
        etcdContainer.start()
        String key = "foo"
        String value = "bar"
        int numPuts = 10
        List<ByteSequence> retFromPutList = new ArrayList<>(numPuts)
        List<ByteSequence> retFromGetList = new ArrayList<>(numPuts)

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(2379)}")
        KVService kvService = new KVService(config)
        ByteSequence expectedFromPut = null

        when:
        for (int i = 0; i < numPuts; i++) {
            String tempKey = key + "-" + i
            String tempValue = value + "-" + i
            ByteSequence retFromPut = kvService.put(ByteSequence.from(tempKey.getBytes()),
                    ByteSequence.from(tempValue.getBytes()))
            retFromPutList.add(retFromPut)
            retFromGetList.add(kvService.get(ByteSequence.from(tempKey, UTF_8)))
        }

        then:
        expectedFromPut ==  retFromPutList.get(0)
        ByteSequence v = retFromGetList.get(0)
        "${value}-0".getBytes() == ((ByteString)v.getAt("byteString")).toByteArray()

        cleanup:
        etcdContainer.stop()
    }
}
