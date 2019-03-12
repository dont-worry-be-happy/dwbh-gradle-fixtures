/*
 * Copyright (C) 2019 Kaleidos Open Source SL
 *
 * This file is part of Don't Worry Be Happy (DWBH).
 * DWBH is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DWBH is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DWBH.  If not, see <https://www.gnu.org/licenses/>
 */
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
