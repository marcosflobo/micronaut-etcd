package io.micronaut.etcd.kv

import io.micronaut.context.ApplicationContext
import io.micronaut.etcd.config.SingleEtcdFactoryConfig
import io.micronaut.inject.qualifiers.Qualifiers
import spock.lang.Specification

class ConfigurationSpec extends Specification {

    def "test configuration" () {
        given:
        String endpoints = "http://localhost:2379"
        String dummyValue = "test"
        ApplicationContext context = ApplicationContext.run(
                ["spec.name"   : getClass().simpleName
                 ,"etcd.endpoints": [endpoints]
                 ,"etcd.user": dummyValue
                 ,"etcd.password": dummyValue
                 ,"etcd.retryMaxDuration": dummyValue
                 ,"etcd.authority": dummyValue
                ]
        )

        expect:
        context.getBean(SingleEtcdFactoryConfig,
                Qualifiers.byName("etcd")).endpoints.contains(endpoints)
        context.getBean(SingleEtcdFactoryConfig,
                Qualifiers.byName("etcd")).user.contains(dummyValue)
        context.getBean(SingleEtcdFactoryConfig,
                Qualifiers.byName("etcd")).password.contains(dummyValue)

        cleanup:
        context.stop()
    }
}
