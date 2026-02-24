val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"
val junitJupiterVersion = "5.9.1"

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "5.0.0.4638"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "eshop"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Web & UI
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	// Development Tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// Lombok (Boilerplate reduction)
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Unit & Integration Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.mockito:mockito-core:5.11.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")

	// Browser Testing (Selenium)
	testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
	testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
	testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")
}

tasks.register<Test>("unitTest") {
	description = "Runs unit tests."
	group = "verification"
	filter {
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests."
	group = "verification"
	filter {
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.test {
	filter {
		excludeTestsMatching("*FunctionalTest")
	}

	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

sonarqube {
	properties {
		property("sonar.projectKey", "B-Sean-Marcello-Maheron-2406401792_Modul-1-Coding-Standards")
		property("sonar.organization", "b-sean-marcello-maheron-2406401792")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}