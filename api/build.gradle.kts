dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("org.mongodb:bson:3.4.3")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("io.netty:netty-transport:4.1.106.Final")
}

var majorVersion = "1"
var minorVersion = "0"
var patchVersion = "0"
var extraData = "-BETA-pre1"

project.version = majorVersion.plus(".").plus(minorVersion).plus(".").plus(patchVersion).plus(extraData)