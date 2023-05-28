import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
}

group = "org.ex"
version = "1.0"

repositories {
    mavenCentral()
}

val kotlinVersion = "1.6.21"


dependencies {
    implementation("org.apache.pdfbox:pdfbox:2.0.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    archiveFileName.set("pdf.jar")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}