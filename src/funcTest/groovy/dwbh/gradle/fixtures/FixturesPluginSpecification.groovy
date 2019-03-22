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

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Checks the plugin configuration
 *
 * @since 0.1.0
 */
@SuppressWarnings('DuplicateStringLiteral')
class FixturesPluginSpecification extends Specification {

	private static final String FIXTURES_DIR = 'fixtures'
	private static final String TASK_NAME_LOAD = 'fixtures-load'

	@Rule TemporaryFolder testProjectDir = new TemporaryFolder()

	File buildFile
	File configFile
	File fixturesLoadDir
	File fixturesCleanDir

	void setup() {
		fixturesLoadDir = testProjectDir.newFolder(FIXTURES_DIR, 'load')
		fixturesCleanDir = testProjectDir.newFolder(FIXTURES_DIR, 'clean')
		configFile = testProjectDir.newFile('fixtures.yml')

		buildFile = testProjectDir.newFile('build.gradle')
		buildFile << '''
            plugins {
                id 'net.kaleidos.dwbh.gradle-fixtures-plugin'
            }
        '''
	}

	void "fixtures-load: can successfully use with no sql files"() {
		given: 'a fixtures configuration'
		configFile.text = '''
        dataSource:
          user: username
          password: password
          driverClass: driverClassName
        '''
		buildFile << '''
            repositories {
                mavenCentral()
            }

            dependencies {
                fixtures 'org.postgresql:postgresql:42.2.5'
            }

            fixtures {
                configFile = "fixtures.yml"
            }
        '''

		when: 'executing the fixtures-load task'
		BuildResult result = GradleRunner.create()
				.withProjectDir(testProjectDir.root)
				.withArguments(TASK_NAME_LOAD)
				.withPluginClasspath()
				.build()

		then: 'we should get a successful output'
		result.task(':fixtures-load').outcome == SUCCESS
		result.output.contains('no sql files found')
	}

	void "fixtures-load: failure when no config files"() {
		given: 'a sql file'
		fixturesLoadDir = testProjectDir.newFolder('custom', FIXTURES_DIR, 'load')
		File sqlFile = testProjectDir.newFile('create-table.sql')

		sqlFile << 'CREATE TABLE sample (id number);'

		and: 'a fixtures configuration'
		buildFile << '''
            fixtures {
                loadDir = 'custom/fixtures/load'
            }
        '''
		when: 'executing the fixtures-load task'
		GradleRunner.create()
				.withProjectDir(testProjectDir.root)
				.withArguments(TASK_NAME_LOAD)
				.withPluginClasspath()
				.build()

		then: 'we should get a successful output'
		thrown(UnexpectedBuildFailure)
	}
}
