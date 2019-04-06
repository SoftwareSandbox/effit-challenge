buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.4.RELEASE")
    }
}

apply(plugin = "io.spring.dependency-management")

plugins {
    kotlin("jvm") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21" // https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support
    id("org.springframework.boot") version "2.1.4.RELEASE"
}


repositories {
    mavenCentral()
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

val kotlinTestVersion = "3.3.2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.21")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:$kotlinTestVersion")
    testImplementation("io.kotlintest:kotlintest-extensions-spring:$kotlinTestVersion")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.13")
}

tasks.bootJar {
    baseName = "effit-webapp.jar"
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.processResources {
    dependsOn(":effit-ui:build")
    from ("../effit-ui/dist") {
        into("static")
    }
}