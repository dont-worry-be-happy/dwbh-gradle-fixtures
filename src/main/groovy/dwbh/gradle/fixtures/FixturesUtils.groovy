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

import groovy.transform.Generated
import org.yaml.snakeyaml.Yaml

/**
 * All auxiliary functions
 *
 * @since 0.1.0
 */
final class FixturesUtils {

	@Generated
	private FixturesUtils() {
		// can't be instantiated
	}

	/**
	 * Filters files with name ending with sql
	 *
	 * @since 0.1.0
	 */
	static FilenameFilter onlySqlFiles = { File dir, String name ->
		return name ==~ /.*sql/
	} as FilenameFilter

	/**
	 * Loads a given yaml file and returns a {@link Map}
	 *
	 * @param sqlConfig file containing the yaml
	 * @return a map with all the yaml content
	 * @since 0.2.0
	 */
	static Map<String,?> loadYaml(File sqlConfig) {
		return new Yaml().load(new FileReader(sqlConfig))
	}
}
