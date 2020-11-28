package io.micronaut.etcd.kv

import io.etcd.jetcd.ByteSequence
import io.micronaut.etcd.config.EtcdFactoryConfig
import io.micronaut.etcd.config.SingleEtcdFactoryConfig
import io.micronaut.etcd.util.DummyObject
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.shaded.org.apache.commons.lang.SerializationUtils
import spock.lang.Shared
import spock.lang.Specification

import static com.google.common.base.Charsets.UTF_8


class KVServiceSpec extends Specification {

    int originalPort = 2379

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
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)
        byte[] expected = null

        when:
        byte[] ret = kvService.get(key)

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
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)
        byte[] expectedFromPut = null

        when:
        byte[] retFromPut = kvService.put(key, BigInteger.valueOf(value).toByteArray())
        byte[] ret = kvService.get(key)

        then:
        expectedFromPut ==  retFromPut
        BigInteger.valueOf(value).toByteArray() == ret

        cleanup:
        etcdContainer.stop()
    }

    def "Put byte array" () {
        given:
        etcdContainer.start()
        String key = "foo"
        byte[] value = "bar".getBytes()

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)

        when:
        kvService.put(key, value)
        byte[] ret = kvService.get(key)

        then:
        value == ret

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
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)

        when:
        kvService.put(key, value)
        byte[] ret = kvService.get(key)
        String retString = new String(ret, UTF_8)

        then:
        value.getBytes() == ret
        retString == value

        cleanup:
        etcdContainer.stop()
    }

    def "Put several String values" () {
        given:
        etcdContainer.start()
        String key = "foo"
        String value = "bar"
        int numPuts = 10
        List<byte[]> retFromGetList = new ArrayList<>(numPuts)

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)

        when:
        for (int i = 0; i < numPuts; i++) {
            String tempKey = key + "-" + i
            String tempValue = value + "-" + i
            kvService.put(tempKey, tempValue)
            retFromGetList.add(kvService.get(tempKey))
        }

        then:
        byte[] v = retFromGetList.get(0)
        "${value}-0".getBytes() == v

        cleanup:
        etcdContainer.stop()
    }

    def "Put single Object" () {
        given:
        etcdContainer.start()
        String key = "foo"
        DummyObject dummyObject = new DummyObject("bar", 69)
        byte[] value = SerializationUtils.serialize(dummyObject)

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)

        when:
        kvService.put(key, value)
        byte[] ret = kvService.get(key)
        DummyObject retDummyObject = SerializationUtils.deserialize(ret) as DummyObject

        then:
        retDummyObject.getField1() == dummyObject.getField1()
        retDummyObject.getField2() == dummyObject.getField2()

        cleanup:
        etcdContainer.stop()
    }

    def "delete element from etc" () {
        given:
        etcdContainer.start()
        String key = "foo"
        String value = "bar"
        long expectedDeletedElements = 1

        and:
        EtcdFactoryConfig config = new SingleEtcdFactoryConfig()
        config.setEndpoints("http://localhost:${etcdContainer.getMappedPort(originalPort)}")
        KVService kvService = new KVService(config)

        when:
        kvService.put(key, value)
        byte[] ret = kvService.get(key)
        String retString = new String(ret, UTF_8)

        then:
        retString == value

        when:
        long numElementsDeleted = kvService.delete(key)

        then:
        expectedDeletedElements == numElementsDeleted

        when:
        ret = kvService.get(key)

        then:
        null == ret

        cleanup:
        etcdContainer.stop()
    }
}
