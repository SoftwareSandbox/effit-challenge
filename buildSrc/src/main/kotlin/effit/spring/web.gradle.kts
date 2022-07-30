package effit.spring

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("effit.kotlin.common")
    id("effit.spring.common")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
