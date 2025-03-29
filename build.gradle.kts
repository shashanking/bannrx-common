plugins {
	java
	id("java-library")
	id("org.springframework.boot") version "3.4.3" apply false
	id("io.spring.dependency-management") version "1.1.7"
	id("maven-publish")
}

group = "com.bannrx"
version = "addUser-0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencyManagement {
	imports{
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

publishing {
	repositories {
		val user: String? = project.findProperty("username") as String? ?: System.getenv("GITHUB_USERNAME")
		val token: String? = project.findProperty("token") as String? ?: System.getenv("GITHUB_TOKEN")
		val repo = "bannrx-common"
		val gitUrl = "https://maven.pkg.github.com/${user}/${repo}"
		maven {
			name = "bannrx-common-package"
			url = uri(gitUrl)
			credentials {
				username = user
				password = token
			}
		}
	}
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}


repositories {
	mavenCentral()
	var env = project.findProperty("environment") as String? ?: System.getenv("ENVIRONMENT")
	if (env != "local"){
		val user: String? = project.findProperty("utility-username") as String? ?: System.getenv("GITHUB_USERNAME")
		val token: String? = project.findProperty("utility-token") as String? ?: System.getenv("GITHUB_TOKEN")
		val repo = "utility"
		val gitUrl = "https://maven.pkg.github.com/${user}/${repo}"
		maven {
			name = "GitHubPackages"
			url = uri(gitUrl)
			credentials {
				username = user
				password = token
			}
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.modelmapper:modelmapper:3.1.1")
	implementation("jakarta.validation:jakarta.validation-api:3.0.1")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
	implementation("org.apache.commons:commons-lang3:3.14.0")


	//added
	var env = project.findProperty("environment") as String? ?: System.getenv("ENVIRONMENT")
	if (env == "local"){
		implementation(project(":utility"))
	} else {
		implementation("com.rklab:utility:addUser-0.0.1-SNAPSHOT")
	}
	implementation("org.springframework.boot:spring-boot-starter-security:3.1.0")
	implementation("org.springframework.security:spring-security-config:6.0.0")
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.2")
	testImplementation("org.mockito:mockito-core:5.14.2")
	testImplementation("org.mockito:mockito-junit-jupiter:5.14.2")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
