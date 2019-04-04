tasks.register("copyWebApplicationJar", type = Copy::class) {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"
    
    dependsOn(project(":effit-backend").tasks.build)

    from("../effit-backend/build/libs/effit-webapp.jar")
    into("webapp")
}

tasks.register("build") {
    group = "build"
    description = "Copy web application jar to be picked up by Docker"
    dependsOn("copyWebApplicationJar")
}