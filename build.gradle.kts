plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.wickeddroid"
version = "1.0-SNAPSHOT"

subprojects {

    if(project.name.contains("api")) {
        apply(plugin = "maven-publish")
    }

    group = "io.github.wickeddroid."+project.name;
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.codemc.io/repository/nms/")
        maven("https://repo.unnamed.team/repository/unnamed-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}