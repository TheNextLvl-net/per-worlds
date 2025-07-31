plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava {
    options.release.set(21)
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    api("net.thenextlvl.core:nbt:2.3.2")
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "per-worlds"
        groupId = "net.thenextlvl"
        pom.url.set("https://thenextlvl.net/docs/perworlds")
        pom.scm {
            val repository = "TheNextLvl-net/per-worlds"
            url.set("https://github.com/$repository")
            connection.set("scm:git:git://github.com/$repository.git")
            developerConnection.set("scm:git:ssh://github.com/$repository.git")
        }
        from(components["java"])
    }
    repositories.maven {
        val branch = if (version.toString().contains("-pre")) "snapshots" else "releases"
        url = uri("https://repo.thenextlvl.net/$branch")
        credentials {
            username = System.getenv("REPOSITORY_USER")
            password = System.getenv("REPOSITORY_TOKEN")
        }
    }
}