plugins {
    id("java")
}

group = "com.valorant"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("org.hibernate:hibernate-core:6.5.0.Final")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    testImplementation(project(":utilities"))
    implementation(project(":utilities"))
    implementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}