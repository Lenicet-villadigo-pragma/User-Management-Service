rootProject.name = "User-Management-Service"

include(
    "user-management-service-domain",
    "user-management-service-application",
    "user-management-service-infrastructure"
)

project(":user-management-service-domain").projectDir = file("user-management-service-domain")
project(":user-management-service-application").projectDir = file("user-management-service-application")
project(":user-management-service-infrastructure").projectDir = file("user-management-service-infrastructure")