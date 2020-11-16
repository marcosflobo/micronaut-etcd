package dummy

import spock.lang.Specification

class DummySpec extends Specification {

    def "dummy test until having more"() {
        given: "a test"

        expect:
        1 == 1
    }
}
