plugins {
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("io.papermc.paperweight.userdev") version "1.5.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

dependencies {
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("me.catcoder:bukkit-sidebar:6.2.0-SNAPSHOT")

    implementation("me.fixeddev:commandflow-bukkit:0.5.2")
    implementation("team.unnamed:inject:2.0.0")

    implementation(project(":api"))
    implementation(project(":annotation-processor"))
    annotationProcessor(project(":annotation-processor"))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "build/generated/sources/annotationProcessor/java/main")
        }
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    runServer {
        minecraftVersion("1.18.2")
    }
    shadowJar {
        val packageName = "io.github.wickeddroid.libs"

        archiveBaseName.set("uhc")
        archiveVersion.set("1.0-SNAPSHOT")

        relocate("org.spongepowered", "$packageName.sponge")
        relocate("me.fixeddev", "$packageName.injector")
        relocate("team.unnamed", "$packageName.command-flow")
        relocate("me.catcoder", "$packageName.siderbar")
    }
}

bukkit {
    main = "io.github.wickeddroid.plugin.UhcPlugin"
    name = "uhc-plugin"
    version = "1.0-SNAPSHOT"
    apiVersion = "1.17"
    author = "Wicked"
}