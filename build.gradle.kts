plugins {
    java
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val junitVersion = "5.6.2"

    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation(kotlin("test"))
}

sourceSets.main{
    java.srcDirs("src")
}

sourceSets.test{
    java.srcDirs("test")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.shadowJar {
    archiveBaseName.set("treePractice")
    archiveClassifier.set("")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "treePracticeKt"
    }
}

val runJar by tasks.creating(Exec::class) {
    dependsOn(tasks.shadowJar)
    val argvString = project.findProperty("argv") as String? ?: ""
    val jarFile = tasks.shadowJar.get().outputs.files.singleFile
    val evalArgs = listOf("java", "-jar", jarFile.absolutePath) + argvString.split(" ")
    commandLine(*evalArgs.toTypedArray())
}