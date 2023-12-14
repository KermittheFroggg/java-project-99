
plugins {
	java
	application
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("io.freefair.lombok") version "8.4"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}

repositories {
	mavenCentral()
}

dependencies {

//	implementation("org.mapstruct:mapstruct:1.5.5.Final")
//	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
//	runtimeOnly("com.h2database:h2")
//	implementation ("org.postgresql:postgresql:42.3.1")
//	implementation("org.springframework.boot:spring-boot-starter")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-validation")
//	implementation("org.springframework.boot:spring-boot-devtools")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("net.datafaker:datafaker:2.0.2")
//	implementation("org.instancio:instancio-junit:3.3.1")
//	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
//
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//	testImplementation("org.springframework.security:spring-security-test")
//
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation(platform("org.junit:junit-bom:5.10.0"))
//	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
//	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
//	testImplementation ("com.h2database:h2")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")

	// implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.2.0")

	implementation("org.instancio:instancio-junit:3.6.0")
	implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	implementation("net.datafaker:datafaker:2.0.2")

	runtimeOnly("com.h2database:h2:2.2.224")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(platform("org.junit:junit-bom:5.10.1"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")


}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	systemProperty("spring.profiles.active", "dev")
}

tasks.test {
	testLogging {
		events("passed", "skipped", "failed")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

buildscript {
	repositories {
		mavenCentral()
	}
}
