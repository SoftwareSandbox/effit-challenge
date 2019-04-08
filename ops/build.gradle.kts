tasks.register("copyWebApplicationJar", Copy::class) {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"

    dependsOn(":effit-backend:build")

    from("../effit-backend/build/libs/effit-webapp.jar")
    into("webapp")
}

tasks.register("build") {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"
    dependsOn("copyWebApplicationJar")
}