package effit.db

plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.postgresql:postgresql:42.2.18")

    implementation(platform("org.jdbi:jdbi3-bom:3.18.0"))
    implementation("org.jdbi:jdbi3-core")
    implementation("org.jdbi:jdbi3-kotlin")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject")
    implementation("org.jdbi:jdbi3-postgres")
    implementation("org.jdbi:jdbi3-spring4")

    implementation("org.springframework.boot:spring-boot-starter-jdbc") // because we're using `@ConfigurationProperties`
}