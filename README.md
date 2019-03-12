[![Travis](https://travis-ci.org/dont-worry-be-happy/dwbh-fixtures-gradle-plugin.svg?branch=master)](https://travis-ci.org/dont-worry-be-happy/dwbh-fixtures-gradle-plugin) 
[![License](https://img.shields.io/github/license/dont-worry-be-happy/dwbh-fixtures-gradle-plugin.svg)](https://www.gnu.org/licenses/gpl-3.0.en.html)

# DWBH FIXTURES GRADLE PLUGIN

This plugin allows you to execute sql files against a configured database. It differentiates
between sql files to `load data` and those sql to `clean data`.

## Installation

Using the plugins DSL:

```groovy
plugins {
   id 'dwbh.gradle.fixtures'
}
```

## Configuration:

Although the plugin defines by default where to find the sql files to load/clean data and where to find
the database configuration file, you can use the `fixtures` extension to override those values:

```groovy
fixtures {
   loadDir = 'customDir/load'
   cleanDir = 'customDir/clean'
   configFile = 'customDir/db.yml'
}
```

Here are the definitions of each field:

|    Name    |                            Description                  |         default       |
| ---------- |:-------------------------------------------------------:| ---------------------:|
| loadDir    | Where to put the fixtures SQL files                     | fixtures/load         |
| cleanDir   | Directory where to find the sql files to clean fixtures | fixtures/clean        |
| configFile | File to get the database connection from                | fixtures/fixtures.yml |

## Loading fixtures

Once you have configured the plugin you can just execute:

```shell
./gradlew fixtures-load
```

And all `sql` files found at the `loadDir` will be executed against the
configured database. Sql files **are executed in order by name**.

## Cleaning fixtures

You can execute:

```shell
./gradlew fixtures-clean
```

And all `sql` files found at the `cleanDir` will be executed against the
configured database. In this case sql files **are executed in reverse order**.

## Acknowledgments

Thanks to [Kaleidos Open Source](https://kaleidos.net/) to make this project possible.