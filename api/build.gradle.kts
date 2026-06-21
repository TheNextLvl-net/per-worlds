plugins {
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.26.+")
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