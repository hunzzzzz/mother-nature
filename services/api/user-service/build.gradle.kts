extra["jwtVersion"] = "0.12.6"

dependencies {
    // import 'common' module
    implementation(project(":common"))
    // database
    runtimeOnly("com.mysql:mysql-connector-j")
    // jwt
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jwtVersion")}")
    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // springboot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}