plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    google() // Required for Google libraries like Firebase
    maven { url = uri("https://dl.google.com/dl/android/maven2/") } // Extra Maven repository for Google dependencies
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:30.0.0"))

    // Firebase Admin SDK and Firestore
    implementation("com.google.firebase:firebase-admin:9.1.1")
    implementation("com.google.cloud:google-cloud-firestore:3.10.0")

    // Other Firebase dependencies if needed
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("io.grpc:grpc-netty-shaded:1.60.0") // Adjust to the latest stable version
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.61.Final")


}



tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
}
