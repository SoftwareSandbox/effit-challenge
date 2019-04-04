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

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.21")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.21")

    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("junit:junit:4.12")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.13")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
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