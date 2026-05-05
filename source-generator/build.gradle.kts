version = "1.0.2"

dependencies {
    implementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("com.palantir.javapoet:javapoet:0.15.0")
    implementation("net.thenextlvl:nbt:4.3.4")
    implementation(project(":api"))
}

tasks.register<JavaExec>("generateSources") {
    group = "build"
    mainClass.set("net.thenextlvl.perworlds.generator.SourceGenerator")
    classpath(sourceSets.main.map { it.runtimeClasspath })
    args(rootProject.layout.projectDirectory.dir("plugin/src/generated/java").asFile.absolutePath)
}
