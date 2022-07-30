package effit.spring

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("effit.kotlin.common")
    id("effit.spring.common")
}

dependencies {
    val springCtxVersion = dependencyManagement.managedVersions["org.springframework:spring-context"]
    implementation("org.springframework:spring-context:$springCtxVersion")
}
