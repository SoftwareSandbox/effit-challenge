package effit.spring

import org.gradle.kotlin.dsl.dependencies

plugins {
    id("effit.kotlin.common")
    id("effit.spring.common")
}

dependencies {
    val springTxVersion = dependencyManagement.managedVersions["org.springframework:spring-tx"]
    implementation("org.springframework:spring-tx:$springTxVersion")
}
