buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.4.RELEASE")
    }
}

plugins {
    id("effit.kotlin.application")
    id("effit.spring.boot")
    id("effit.db.jdbi")
}

repositories {
    mavenCentral()
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

val junit5Version = "5.4.1"

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.flywaydb:flyway-core:5.2.4")
    implementation("org.postgresql:postgresql:42.2.5")
    
    testImplementation("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:$junit5Version")
    testImplementation("org.mockito:mockito-junit-jupiter")
}

//bootJar
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("effit-cqrs-webapp")
//    mainClass.set("be.swsb.effit.EffitCqrsApplication")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
