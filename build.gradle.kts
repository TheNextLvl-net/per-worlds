import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("idea")
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("com.modrinth.minotaur") version "2.+"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.compileJava {
    options.release.set(21)
}

group = "net.thenextlvl.perworlds"
version = "1.2.0"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.thenextlvl.net/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT")

    compileOnly("net.thenextlvl:worlds:3.8.0-pre7") { isTransitive = false }

    implementation("net.thenextlvl.core:adapters:2.0.3")
    implementation("net.thenextlvl.core:i18n:3.2.2")
    implementation("net.thenextlvl.core:paper:2.3.1")
    implementation("net.thenextlvl:nbt:3.0.1")
    implementation("org.bstats:bstats-bukkit:3.1.0")

    implementation(project(":api"))

    testImplementation(platform("org.junit:junit-bom:6.1.0-SNAPSHOT"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showCauses = true
        showExceptions = true
    }
}

val generatedPath: java.nio.file.Path = layout.projectDirectory.dir("src/generated/java").asFile.toPath()
idea.module.generatedSourceDirs.add(generatedPath.toFile())
sourceSets.main {
    java.srcDir(generatedPath)
}

tasks.shadowJar {
    archiveBaseName.set("per-worlds")
    relocate("org.bstats", "net.thenextlvl.perworlds.bstats")
}

tasks.compileJava {
    dependsOn(":source-generator:generateSources")
}

paper {
    name = "PerWorlds"
    main = "net.thenextlvl.perworlds.PerWorldsPlugin"
    apiVersion = "1.21.8"
    description = "Per-world customization for gameplay and settings"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    website = "https://thenextlvl.net"
    authors = listOf("NonSwag")
    // foliaSupported = true // way too many events still not being called on folia
    permissions {
        register("perworlds.admin") { children = listOf("perworlds.commands.group") }

        register("perworlds.commands.group") {
            description = "Allows access to all group commands"
            children = listOf(
                "perworlds.command.group.add",
                "perworlds.command.group.auto",
                "perworlds.command.group.create",
                "perworlds.command.group.delete",
                "perworlds.command.group.info",
                "perworlds.command.group.list",
                "perworlds.command.group.migrate",
                "perworlds.command.group.option",
                "perworlds.command.group.remove",
                "perworlds.command.group.spawn.set",
                "perworlds.command.group.spawn.unset",
                "perworlds.command.group.teleport",
            )
        }

        register("perworlds.command.group") { children = listOf("perworlds.command") }
        register("perworlds.command.group.add") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.auto") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.create") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.delete") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.info") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.list") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.migrate") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.option") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.remove") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.spawn") { children = listOf("perworlds.command.group") }
        register("perworlds.command.group.spawn.set") { children = listOf("perworlds.command.group.spawn") }
        register("perworlds.command.group.spawn.unset") { children = listOf("perworlds.command.group.spawn") }
        register("perworlds.command.group.teleport") { children = listOf("perworlds.command.group") }
    }

    serverDependencies {
        register("Worlds") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
    }
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

val versions: List<String> = (property("gameVersions") as String)
    .split(",")
    .map { it.trim() }

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("per-worlds") {
        id.set("PerWorlds")
        version.set(versionString)
        changelog = System.getenv("CHANGELOG")
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(versions)
            dependencies {
                hangar("Worlds") { required.set(false) }
            }
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("lpfQmSV2")
    changelog = System.getenv("CHANGELOG")
    versionType = if (isRelease) "release" else "beta"
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(versions)
    loaders.add("paper")
    dependencies {
        optional.project("worlds-1")
    }
}
