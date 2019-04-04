import com.moowork.gradle.node.npm.NpmTask

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.moowork.node") version "1.2.0"
}

node {
    version = "10.15.0"
    npmVersion = "6.5.0"
    download = true
}

tasks.register("buildUI", NpmTask::class) {
    dependsOn("npmInstall")
    group = "build"
    description = "Compile client side folder for development"
    setArgs(listOf("run", "build"))
}

tasks.register("jestTests", NpmTask::class) {
    dependsOn("buildUI")
    group = "build"
    description = "Run tests"
    setArgs(listOf("run", "test:unit"))
}

tasks.build {
    dependsOn("jestTests")
}