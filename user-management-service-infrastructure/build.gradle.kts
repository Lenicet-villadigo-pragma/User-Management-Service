dependencies {
    implementation(project(":user-management-service-domain"))
    implementation(project(":user-management-service-application"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui")
    implementation("io.r2dbc:r2dbc-pool")
    runtimeOnly("io.asyncer:r2dbc-mysql")
    testImplementation("com.squareup.okhttp3:mockwebserver")
}