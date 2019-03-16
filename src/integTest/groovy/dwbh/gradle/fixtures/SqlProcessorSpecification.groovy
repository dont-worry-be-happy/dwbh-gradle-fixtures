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

import groovy.sql.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import org.gradle.api.logging.Logger

/**
 * Checks {@link SqlProcessor}
 *
 * @since 0.1.0
 */
@Testcontainers
class SqlProcessorSpecification extends Specification {

	static final File SQL_DIR = 'src/test/resources/dwbh/gradle/fixtures' as File
	static final String CONTAINER_SEED = 'dwbh'

	@Shared
	PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
	.withDatabaseName(CONTAINER_SEED)
	.withUsername(CONTAINER_SEED)
	.withPassword(CONTAINER_SEED)

	void 'apply fixtures' () {
		given: 'a database connection configuration'
		Map<String, ?> config = [
			dataSource:[
				url:postgreSQLContainer.jdbcUrl,
				user:postgreSQLContainer.username,
				password:postgreSQLContainer.password,
			],
		]

		and: 'a set of sql files and a mocked logger'
		File[] sqlFiles = SQL_DIR
				.listFiles(FixturesUtils.onlySqlFiles)
				.sort()
		Logger logger = Mock(Logger)

		when: 'executing the processor'
		new SqlProcessor(config, sqlFiles, logger).process()

		and: 'checking how many rows have been added'
		Integer rows = Sql
				.newInstance(config.dataSource)
				.firstRow('SELECT count(*) as counter FROM avengers')
				.get('counter') as Integer

		then: 'we should get the expected row number'
		rows == 3
	}
}
