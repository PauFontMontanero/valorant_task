plugins {
    id("java")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.9")
    implementation(project(":utilities"))
    implementation(project(":domain"))
    implementation(project(":domain-implementations:jdbc"))
    implementation(project(":domain-implementations:jpa"))
    implementation("de.vandermeer:asciitable:0.3.2")

    // JPA and Hibernate dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("jakarta.persistence:jakarta.persistence-api:3.0.0")
    implementation("org.hibernate:hibernate-core:5.6.10.Final")
}

application {
    mainClass.set("com.valorant.backoffice.App")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
