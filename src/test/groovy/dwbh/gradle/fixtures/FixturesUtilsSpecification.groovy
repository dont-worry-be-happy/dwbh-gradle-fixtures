package dwbh.gradle.fixtures

import spock.lang.Specification

/**
 * Checks {@link FixturesUtils}
 *
 * @since 0.1.0
 */
class FixturesUtilsSpecification extends Specification {

    static final File CONFIG_FILE = 'src/test/resources/dwbh/gradle/fixtures/config.yml' as File

    void 'load yaml file'() {
        when: 'loading a specific yaml CONFIG_FILE'
        Map<String,?> config = FixturesUtils.loadYaml(CONFIG_FILE)

        then: 'we should load the expected information'
        with(config.dataSource) {
            user            == 'user'
            password        == 'password'
            driverClassName == 'driverClassName'
        }
    }
}
