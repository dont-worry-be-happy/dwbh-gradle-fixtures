package dwbh.gradle.fixtures

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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
	String group = 'fixtures'

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
