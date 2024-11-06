plugins {
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.8-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.+"
    id("maven-publish")
}

version = project.properties["mod_version"].toString()

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21


loom {
    // Split minecraft sources
    //splitEnvironmentSourceSets()

    runtimeOnlyLog4j.set(true)
}

repositories {
    maven("https://maven.kosmx.dev/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    mavenCentral()
    maven("https://api.modrinth.com/maven"){
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")

    // Yarn is better documented than mojmap.
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["fabric_kotlin"]}")

    // We might want advanced player animations
    modImplementation("dev.kosmx.player-anim:player-animation-lib-fabric:${project.properties["player_anim"]}")

    modLocalRuntime("com.terraformersmc:modmenu:${project.properties["mod_menu"]}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.properties["cloth_config"]}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modCompileOnly("maven.modrinth:noemotecraft:${project.properties["noemotecraft"]}")

    modImplementation("maven.modrinth:replaymod:${project.properties["replaymod"]}")

}

base {
    archivesName.set(project.properties["archive_base_name"].toString())
}

kotlin {
    jvmToolchain(21)
}

tasks {

    processResources {
        inputs.property("version", project.version)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    java {
        // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
        // if it is present.
        // If you remove this line, sources will not be generated.
        withSourcesJar()
    }


    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }

}
