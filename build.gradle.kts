plugins {
    kotlin("jvm") version "1.6.10"
    id("fabric-loom")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
    java
}

group = property("maven_group")!!

repositories {
    mavenCentral()
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    //implementation("com.github.wagyourtail:baritone:ver")
    implementation("org.luaj:luaj-jse:${property("luaj_version")}")
    implementation("com.github.NepNep21:DiscordRPC4j16:${property("discord_version")}")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${property("kotlin_version")}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${property("kotlin_version")}")
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
    //withSourcesJar()
}

tasks.shadowJar {
    archiveClassifier.set("")
    dependencies {
        include(dependency("org.luaj:luaj-jse:.*"))
        include(dependency("com.github.NepNep21:DiscordRPC4j16:.*"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:.*"))
        include(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
