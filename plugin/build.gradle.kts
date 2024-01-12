plugins {
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("io.papermc.paperweight.userdev") version "1.5.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

var majorVersion = "1"
var minorVersion = "0"
var patchVersion = "2"

project.version = majorVersion.plus(".").plus(minorVersion).plus(".").plus(patchVersion).plus("-BETA")

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("me.catcoder:bukkit-sidebar:6.2.5-SNAPSHOT")

    implementation("com.github.agus5534.gui:gui-menu-api:45e66ff34e")

    implementation("me.fixeddev:commandflow-bukkit:0.5.2")
    implementation("team.unnamed:inject:2.0.0")

    arrayOf("1_19_R1", "1_19_R2", "1_19_R3", "1_20_R1", "1_20_R2").forEach {
        implementation("com.github.agus5534.gui:gui-menu-adapt-v$it:45e66ff34e:dev")
    }

    implementation("commons-io:commons-io:2.13.0")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")

    implementation(project(":api"))
    implementation(project(":annotation-processor"))
    annotationProcessor(project(":annotation-processor"))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
    runServer {
        minecraftVersion("1.19.4")
    }
    shadowJar {
        val packageName = "io.github.wickeddroid.libs"

        archiveBaseName.set(rootProject.name)
        archiveVersion.set(rootProject.version.toString())

        relocate("commons-io", "$packageName.commons")
        relocate("org.spongepowered", "$packageName.sponge")
        relocate("me.fixeddev", "$packageName.injector")
        relocate("team.unnamed", "$packageName.team.unnamed")
        relocate("me.catcoder", "$packageName.siderbar")
    }
}

tasks.reobfJar {
    outputJar.set(layout.buildDirectory.file("libs/${rootProject.name}-${project.version}.jar"))
}

bukkit {
    main = "io.github.wickeddroid.plugin.UhcPlugin"
    name = rootProject.name
    version = project.version.toString()
    apiVersion = "1.19"
    author = "Wicked"
}