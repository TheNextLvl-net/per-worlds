version = "1.0.2"

dependencies {
    implementation("io.papermc.paper:paper-api:26.2.build.+")
    implementation("com.palantir.javapoet:javapoet:0.16.0")
    implementation("net.thenextlvl:nbt:4.3.6")
    implementation(project(":api"))
}

tasks.register<JavaExec>("generateSources") {
    group = "build"
    mainClass.set("net.thenextlvl.perworlds.generator.SourceGenerator")
    classpath(sourceSets.main.map { it.runtimeClasspath })
    args(rootProject.layout.projectDirectory.dir("plugin/src/generated/java").asFile.absolutePath)
}
