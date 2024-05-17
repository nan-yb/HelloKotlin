// For `KotlinCompile` task below
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("org.springframework.boot:spring-boot-starter-validation")
    implementation ("org.springframework.boot:spring-boot-starter-data-r2dbc")
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("io.projectreactor:reactor-test")
    implementation ("org.mapstruct:mapstruct:1.5.1.Final")
    annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.1.Final")
    runtimeOnly ("io.r2dbc:r2dbc-h2")
    implementation ("com.google.code.gson:gson")
    implementation ("org.apache.commons:commons-lang3:3.12.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    
//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
//    implementation("org.springframework.boot:spring-boot-starter-mustache")
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("io.projectreactor:reactor-test")
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//
//    runtimeOnly("io.r2dbc:r2dbc-h2")
//    implementation("org.mapstruct:mapstruct:1.5.1.Final")
//    annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.1.Final")

//    implementation("org.redisson:redisson:3.13.0")

//
//    runtimeOnly ("com.mysql:mysql-connector-j")
//
//    implementation ("io.asyncer:r2dbc-mysql:1.0.4")
//    implementation("dev.miku:r2dbc-mysql:0.8.1.RELEASE")
//    testRuntimeOnly ("com.h2database:h2")
//    testImplementation ("io.r2dbc:r2dbc-h2")
//
//    runtimeOnly("org.springframework.boot:spring-boot-devtools")
//
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
//    testImplementation("com.ninja-squad:springmockk:4.0.2")
//
//
//    compileOnly ("org.projectlombok:lombok")
//    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
//    annotationProcessor ("org.projectlombok:lombok")
}



tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}
