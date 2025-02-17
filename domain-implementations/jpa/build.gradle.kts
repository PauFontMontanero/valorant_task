/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.java-library-conventions")
}

dependencies{
    implementation(project(":domain"))
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("com.h2database:h2:1.4.200")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation(project(":utilities"))
    implementation("org.hibernate:hibernate-core:6.5.0.Final")
}