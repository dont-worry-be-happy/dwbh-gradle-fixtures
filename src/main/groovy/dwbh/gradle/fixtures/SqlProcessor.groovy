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
import groovy.transform.TupleConstructor
import org.gradle.api.logging.Logger

/**
 * Responsible for loading sql statements found in the
 * files located at the fixtures directory
 *
 * @since 0.1.0
 */
@TupleConstructor
class SqlProcessor {

	/**
	 * Database configuration
	 *
	 * @since 0.1.0
	 */
	Map<String,?> config

	/**
	 * Sql files to load
	 *
	 * @since 0.1.0
	 */
	File[] sqlFiles

	/**
	 * Gradle logger to trace the process
	 *
	 * @since 0.1.0
	 */
	Logger logger

	/**
	 * Loads all fixtures found
	 *
	 * @since 0.1.0
	 */
	void process() {
		logger.debug '> Fixtures: configuring sql access'
		Sql sql = Sql.newInstance(config.dataSource)

		logger.debug '> Fixtures: reading fixtures files'

		sqlFiles.each { File sqlFile ->
			logger.lifecycle "> Fixtures: applying: ${sqlFile.name}"

			String[] queries = sqlFile.readLines()

			logger.debug "> Fixtures: processing ${queries.size()} lines"

			sql.withBatch(queries.size()) { stmt ->
				queries.each { String query ->
					logger.debug "> Fixtures: query: $query"
					stmt.addBatch(query)
				}
			}
		}

		logger.lifecycle '-------------------------------------------------'
		logger.lifecycle "> Fixtures: applied: ${sqlFiles.size()} sql files"
	}
}
