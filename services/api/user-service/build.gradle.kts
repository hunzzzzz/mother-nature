dependencies {
    // import 'common' module
    implementation(project(":common"))
    // database
    runtimeOnly("com.mysql:mysql-connector-j")
    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // springboot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
}