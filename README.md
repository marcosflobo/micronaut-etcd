# Micronaut etcd

[![Maven Central](https://img.shields.io/maven-central/v/io.micronaut.etcd/micronaut-etcd.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.micronaut.etcd%22%20AND%20a:%22micronaut-etcd%22)
[![Build Status](https://github.com/marcosflobo/micronaut-etcd/workflows/Java%20CI/badge.svg)](https://github.com/marcosflobo/micronaut-etcd/actions)

This project integrates [etcd](https://etcd.io/) and [Micronaut](https://micronaut.io) 

## Documentation

See the [Documentation](https://micronaut-projects.github.io/micronaut-etcd/latest/guide/) for more information. 

See the [Snapshot Documentation](https://micronaut-projects.github.io/micronaut-etcd/snapshot/guide/) for the current development docs.

## Examples

Examples can be found in the [examples](https://github.com/marcosflobo/micronaut-etcd/tree/master/examples) directory.

## Snapshots and Releases

Snaphots are automatically published to [JFrog OSS](https://oss.jfrog.org/artifactory/oss-snapshot-local/) using [Github Actions](https://github.com/micronaut-projects/micronaut-etcd/actions).

See the documentation in the [Micronaut Docs](https://docs.micronaut.io/latest/guide/index.html#usingsnapshots) for how to configure your build to use snapshots.

Releases are published to JCenter and Maven Central via [Github Actions](https://github.com/micronaut-projects/micronaut-etcd/actions).

Releases are completely automated. To perform a release use the following steps:

* [Publish the draft release](https://github.com/marcosflobo/micronaut-etcd/releases). There should be already a draft release created, edit and publish it. The Git Tag should start with `v`. For example `v1.0.0`.
* [Monitor the Workflow](https://github.com/marcosflobo/micronaut-etcd/actions?query=workflow%3ARelease) to check it passed successfully.
* Celebrate!
