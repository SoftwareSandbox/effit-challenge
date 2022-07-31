package effit.spring

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("effit.kotlin.common")
    id("effit.spring.common")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    testImplementation("org.springframework.security:spring-security-test")
}
