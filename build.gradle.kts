plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.wickeddroid"
version = "1.0-SNAPSHOT"

subprojects {
    group = "io.github.wickeddroid."+project.name;
    apply(plugin = "maven-publish")
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                from(components["java"])
            }
        }
    }

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