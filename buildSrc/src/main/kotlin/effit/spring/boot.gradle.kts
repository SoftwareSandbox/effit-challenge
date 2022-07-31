package effit.spring

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("effit.kotlin.common")
    id("effit.spring.web")
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.micrometer:micrometer-registry-prometheus")
}
