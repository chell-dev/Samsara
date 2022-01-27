plugins {
    kotlin("jvm") version "1.6.10"
    id("fabric-loom")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
    java
}

group = property("maven_group")!!

val minecraftVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project

val luajVersion: String by project
val discordVersion: String by project

val kotlinVersion: String by project
val coroutinesVersion: String by project
val serializationVersion: String by project

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    //implementation("com.github.wagyourtail:baritone:ver")
    use("org.luaj:luaj-jse:$luajVersion")
    use("com.github.NepNep21:DiscordRPC4j16:$discordVersion")

    // kotlin
    use(kotlin("stdlib", kotlinVersion))
    use(kotlin("stdlib-jdk8", kotlinVersion))
    use(kotlin("stdlib-jdk7", kotlinVersion))
    use(kotlin("reflect", kotlinVersion))
    //use("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    //use("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
    //use("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    //use("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:$serializationVersion")
    //use("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$serializationVersion")
    //use("org.jetbrains.kotlinx:kotlinx-serialization-cbor-jvm:$serializationVersion")
}

fun DependencyHandlerScope.use(dep: Any) {
    include(dep)
    implementation(dep)
}

tasks {

    processResources {
        val jsonFile = file("src/main/resources/fabric.mod.json")
        val parsedJson = groovy.json.JsonSlurper().parseText(jsonFile.readText()) as Map<*, *>
        version = parsedJson["version"]!!
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                //artifact(kotlinSourcesJar) {
                //    builtBy(remapSourcesJar)
                //}
            }
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

}

java {
    // withSourcesJar()
}
