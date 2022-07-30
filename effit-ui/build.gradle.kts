import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.4.0"
}

node {
    version.set("10.15.0")
    npmVersion.set("6.5.0")
    download.set(true)
}

tasks.register("buildUI", NpmTask::class) {
    dependsOn("npmInstall")
    group = "build"
    description = "Compile client side folder for development"
    args.set(listOf("run", "build"))
}

tasks.register("build", NpmTask::class) {
    dependsOn("buildUI")
    group = "build"
    description = "Run tests"
    args.set(listOf("run", "test:unit"))
}
