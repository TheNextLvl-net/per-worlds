plugins {
    id("java")
}

group = "net.thenextlvl.perworlds"
version = "1.0.2"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.thenextlvl.net/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("com.palantir.javapoet:javapoet:0.10.0")
    implementation("net.thenextlvl:nbt:4.3.2")
    implementation(project(":api"))

    testImplementation(platform("org.junit:junit-bom:6.1.0-SNAPSHOT"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.register<JavaExec>("generateSources") {
    group = "build"
    mainClass.set("net.thenextlvl.perworlds.generator.SourceGenerator")
    classpath(sourceSets.main.map { it.runtimeClasspath })
    args(rootProject.layout.projectDirectory.dir("src/generated/java").asFile.absolutePath)
}

tasks.test {
    useJUnitPlatform()
}