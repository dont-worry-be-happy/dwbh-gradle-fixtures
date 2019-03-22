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
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

/**
 * Task responsible for loading configured fixtures
 *
 * @since 0.1.0
 */
class FixturesTask extends DefaultTask {

	/**
	 * Group the task belongs to
	 *
	 * @since 0.1.0
	 */
	String group = FixturesPlugin.EXTENSION_NAME

	/**
	 * Where to put the fixtures SQL files
	 *
	 * @since 0.1.0
	 */
	@InputDirectory
	File inputDir

	/**
	 * Database configuration file
	 *
	 * @since 0.1.0
	 */
	@InputFile
	File configFile

	/**
	 * Indicates if it is a clean operation
	 *
	 * @since 0.1.0
	 */
	boolean isClean = false

	/**
	 * Task entry point
	 *
	 * @since 0.1.0
	 */
	@TaskAction
	void executeTask() {
		enableJdbcDrivers(this.project)

		logger.lifecycle "------------------${this.name.toUpperCase()}-----------------"

		if (!configFile.exists()) {
			throw new GradleException('no database configuration found')
		}

		File[] sqlFiles = isClean ? sqlFilesClean(inputDir) : sqlFilesLoad(inputDir)

		if (!sqlFiles) {
			logger.lifecycle('no sql files found')
			return
		}

		processFiles(configFile, sqlFiles)
	}

	private void enableJdbcDrivers(Project project) {
		// https://discuss.gradle.org/t/jdbc-driver-class-cannot-be-loaded-with-gradle-2-0-but-worked-with-1-12/2277/6
		ClassLoader loader = Sql.classLoader

		// enables loading drivers via fixtures configuration
		project.configurations.getByName(FixturesPlugin.EXTENSION_NAME).each { File file ->
			loader.addURL(file.toURI().toURL())
		}
	}

	private void processFiles(File configFile, File[] sqlFiles) {
		Map<String,?> config = FixturesUtils.loadYaml(configFile)
		SqlProcessor processor = new SqlProcessor(config, sqlFiles, logger)

		processor.process()
	}

	private File[] sqlFilesLoad(File inputDir) {
		return inputDir?.listFiles(FixturesUtils.onlySqlFiles)?.sort()
	}

	private File[] sqlFilesClean(File inputDir) {
		return sqlFilesLoad(inputDir)?.reverse()
	}
}
