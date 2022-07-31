buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

}

plugins {
    id("effit.kotlin.application")
    id("effit.spring.boot")
    id("effit.spring.security")
    id("effit.db.jpa")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:5.2.4")
    testImplementation("com.h2database:h2")
}

//bootJar
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("effit-cqrs-webapp")
//    mainClass.set("be.swsb.effit.EffitCqrsApplication")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

application {
    mainClass.set("be.swsb.effit.EffitCqrsApplication")
}

//distTar
tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

//distZip
tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.processResources {
    dependsOn(":effit-ui:build")
    from ("../effit-ui/dist") {
        into("static")
    }
}
