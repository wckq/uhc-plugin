rootProject.name = "uhc-plugin"

include("api", "plugin", "annotation-processor")


arrayOf("1_19_R1", "1_19_R2", "1_19_R3", "1_20_R1", "1_20_R2").forEach {
    addAdapter("adapter:v$it")
}

fun addAdapter(n: String) {
    var name = n.replace(':', '-')
    val path = n.replace(':', '/')
    val baseName = "${rootProject.name}-$name"

    include(baseName)
    project(":$baseName").projectDir = file(path)
}