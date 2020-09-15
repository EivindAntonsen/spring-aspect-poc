import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.spring") version "1.4.0"
}

group = "no.esa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val springFrameworkBoot = "org.springframework.boot"

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("$springFrameworkBoot:spring-boot-starter")
    implementation("$springFrameworkBoot:spring-boot-starter-aop")
    implementation("$springFrameworkBoot:spring-boot-starter-jdbc")
    implementation("$springFrameworkBoot:spring-boot-starter-web")
    developmentOnly("$springFrameworkBoot:spring-boot-devtools")
    annotationProcessor("$springFrameworkBoot:spring-boot-configuration-processor")
    testImplementation("$springFrameworkBoot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation(group = "io.springfox", name = "springfox-swagger2", version = "2.9.2")
    implementation(group = "io.springfox", name = "springfox-swagger-ui", version = "2.9.2")
    runtimeOnly("com.h2database:h2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
