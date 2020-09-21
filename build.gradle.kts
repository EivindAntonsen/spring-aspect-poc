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
	val springBoot = "org.springframework.boot"
	val swagger = "io.springfox"
	val jetBrains = "org.jetbrains.kotlin"

	implementation(group = jetBrains, name = "kotlin-reflect")
	implementation(group = jetBrains, name = "kotlin-stdlib-jdk8")
	implementation("$springBoot:spring-boot-starter")
	implementation("$springBoot:spring-boot-starter-validation")
	implementation("$springBoot:spring-boot-starter-aop")
	implementation("$springBoot:spring-boot-starter-jdbc")
	implementation("$springBoot:spring-boot-starter-web")
	developmentOnly("$springBoot:spring-boot-devtools")
	annotationProcessor("$springBoot:spring-boot-configuration-processor")
	testImplementation("$springBoot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	implementation(group = swagger, name = "springfox-swagger2", version = "2.9.2")
	implementation(group = swagger, name = "springfox-swagger-ui", version = "2.9.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	runtimeOnly("com.h2database:h2")
	implementation(group = "org.flywaydb", name = "flyway-core", version = "6.4.1")

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
