plugins {
    id("xyz.jpenilla.run-paper") version "2.0.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-gson:4.1.2")
    implementation("me.catcoder:bukkit-sidebar:6.2.0-SNAPSHOT")

    implementation("me.fixeddev:commandflow-bukkit:0.5.2")
    implementation("team.unnamed:inject:2.0.0")

    implementation(project(":api"))
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
    author = "Wicked"
}