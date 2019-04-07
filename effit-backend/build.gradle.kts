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
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.21"
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21" // https://kotlinlang.org/docs/reference/compiler-plugins.html#spring-support
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.21"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    baseName = "effit-webapp.jar"
    mainClassName = "be.swsb.effit.EffitApplication"
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