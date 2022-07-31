package effit.db

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.noarg")
}

dependencies {
    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

noArg {
    annotations("javax.persistence.Entity","javax.persistence.Embeddable")
}