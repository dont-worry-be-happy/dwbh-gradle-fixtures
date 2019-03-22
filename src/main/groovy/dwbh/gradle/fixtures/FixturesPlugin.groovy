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

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin exposing two tasks to load and truncate the database
 *
 * <ul>
 *     <li>fixtures-load</li>
 *     <li>fixtures-clean</li>
 * </ul>
 *
 * @since 0.1.0
 */
class FixturesPlugin implements Plugin<Project> {

	static final String EXTENSION_NAME = 'fixtures'

	static final String TASK_NAME_CLEAN = 'fixtures-clean'

	static final String TASK_NAME_LOAD = 'fixtures-load'

	/**
	 * Default directory where to find the fixtures to load the database
	 *
	 * @since 0.1.0
	 */
	static final File DEFAULT_CLEAN_DIR = new File('fixtures/clean')

	/**
	 * Default directory where to find the fixtures to clean the database
	 *
	 * @since 0.1.0
	 */
	static final File DEFAULT_LOAD_DIR = new File('fixtures/load')

	/**
	 * Default file to read database configuration from
	 *
	 * @since 0.1.0
	 */
	static final File DEFAULT_CONFIG_FILE = new File('fixtures/fixtures.yml')

	@Override
	void apply(Project project) {
		project.extensions.create(EXTENSION_NAME, FixturesExtension)
		project.configurations.create(EXTENSION_NAME)

		project.tasks.create(TASK_NAME_LOAD, FixturesTask) { it.description = 'loads all fixtures' }
		project.tasks.create(TASK_NAME_CLEAN, FixturesTask) { it.description = 'wipes out all fixtures' }

		project.afterEvaluate(this.&afterEvaluate)
	}

	private void afterEvaluate(Project project) {
		FixturesExtension extension = project
				.extensions
				.getByType(FixturesExtension)

		File configFile = Optional
				.ofNullable(extension.configFile)
				.map { project.file(it) }
				.orElse(DEFAULT_CONFIG_FILE)

		File loadDir = Optional
				.ofNullable(extension.loadDir)
				.map { project.file(it) }
				.orElse(DEFAULT_LOAD_DIR)

		File cleanDir = Optional.ofNullable(extension.cleanDir)
				.map { project.file(it) }
				.orElse(DEFAULT_CLEAN_DIR)

		project.tasks.getByName(TASK_NAME_LOAD).configure { FixturesTask t ->
			t.inputDir = loadDir
			t.configFile = configFile
			t.isClean = false
		}

		project.tasks.getByName(TASK_NAME_CLEAN).configure { FixturesTask t ->
			t.inputDir = cleanDir
			t.configFile = configFile
			t.isClean = true
		}
	}
}
