tasks.register("copyWebApplicationJar", Copy::class) {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"

    dependsOn(":effit-cqrs-backend:build")

    from("../effit-cqrs-backend/build/libs/effit-cqrs-webapp.jar")
    into("webapp")
}

tasks.register("build") {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"
    dependsOn("copyWebApplicationJar")
}
