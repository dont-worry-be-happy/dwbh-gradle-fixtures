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

/**
 * Extension available in project's build.gradle DSL to
 * configure the directory where the fixtures must be
 * located
 *
 * @since 0.1.0
 */
class FixturesExtension {

	/**
	 * Directory where to find the sql files to load fixtures
	 *
	 * @since 0.1.0
	 */
	String loadDir

	/**
	 * Directory where to find the sql files to clean fixtures
	 *
	 * @since 0.1.0
	 */
	String cleanDir

	/**
	 * File to get the database connection from
	 *
	 * @since 0.1.0
	 */
	String configFile
}
