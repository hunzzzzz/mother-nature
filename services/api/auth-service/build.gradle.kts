extra["jwtVersion"] = "0.12.6"

dependencies {
    // import 'common' module
    implementation(project(":common"))
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jwtVersion")}")
}