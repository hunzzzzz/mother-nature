dependencies {
    // import 'common' module
    implementation(project(":common"))
    // springboot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}