import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.paperweight.userdev.attribute.Obfuscation

plugins {
    `java-library`
}

applyPlatformAndCoreConfiguration()
applyShadowConfiguration()

repositories {
    maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

val localImplementation = configurations.create("localImplementation") {
    description = "Dependencies used locally, but provided by the runtime Bukkit implementation"
    isCanBeConsumed = false
    isCanBeResolved = false
}
configurations["compileOnly"].extendsFrom(localImplementation)
configurations["testImplementation"].extendsFrom(localImplementation)

val adapters = configurations.create("adapters") {
    description = "Adapters to include in the JAR"
    isCanBeConsumed = false
    isCanBeResolved = true
    shouldResolveConsistentlyWith(configurations["runtimeClasspath"])
    attributes {
        attribute(Obfuscation.OBFUSCATION_ATTRIBUTE,
            if ((project.findProperty("enginehub.obf.none") as String?).toBoolean()) {
                objects.named(Obfuscation.NONE)
            } else {
                objects.named(Obfuscation.OBFUSCATED)
            }
        )
    }
}

dependencies {
    "api"(project(":worldedit-core"))
    "api"(project(":worldedit-libs:bukkit"))
    // Technically this is api, but everyone should already have some form of the bukkit API
    // Avoid pulling in another one, especially one so outdated.
    "localImplementation"("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT") {
        exclude("junit", "junit")
    }

    "localImplementation"(platform("org.apache.logging.log4j:log4j-bom:${Versions.LOG4J}") {
        because("Spigot provides Log4J (sort of, not in API, implicitly part of server)")
    })
    "localImplementation"("org.apache.logging.log4j:log4j-api")

    "compileOnly"("org.jetbrains:annotations:20.1.0")
    "compileOnly"("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT") {
        exclude("org.slf4j", "slf4j-api")
        exclude("junit", "junit")
    }
    "implementation"("io.papermc:paperlib:1.0.7")
    "compileOnly"("com.sk89q:dummypermscompat:1.10")
    "implementation"("org.bstats:bstats-bukkit:2.2.1")
    "implementation"("it.unimi.dsi:fastutil")
    "implementation"("com.github.Anon8281:UniversalScheduler:0.1.6")
    "testImplementation"("org.mockito:mockito-core:1.9.0-rc1")

    project.project(":worldedit-bukkit:adapters").subprojects.forEach {
        "adapters"(project(it.path))
    }
}

tasks.named<Copy>("processResources") {
    val internalVersion = project.ext["internalVersion"]
    inputs.property("internalVersion", internalVersion)
    filesMatching("plugin.yml") {
        expand("internalVersion" to internalVersion)
    }
}

addJarManifest(WorldEditKind.Plugin, includeClasspath = true)

tasks.named<ShadowJar>("shadowJar") {
    configurations.add(adapters)
    dependencies {
        // In tandem with not bundling log4j, we shouldn't relocate base package here.
        // relocate("org.apache.logging", "com.sk89q.worldedit.log4j")
        relocate("org.antlr.v4", "com.sk89q.worldedit.antlr4")
        // Purposefully not included, we assume (even though no API exposes it) that Log4J will be present at runtime
        // If it turns out not to be true for Spigot/Paper, our only two official platforms, this can be uncommented.
        // include(dependency("org.apache.logging.log4j:log4j-api"))
        include(dependency("org.antlr:antlr4-runtime"))
        include(dependency("org.bstats:"))
        include(dependency("io.papermc:paperlib"))
        include(dependency("it.unimi.dsi:fastutil"))
        include(dependency("com.sk89q.lib:jlibnoise"))
        include(dependency("com.github.Anon8281:"))

        exclude(dependency("$group:$name"))

        relocate("org.bstats", "com.sk89q.worldedit.bstats")
        relocate("io.papermc.lib", "com.sk89q.worldedit.bukkit.paperlib")
        relocate("it.unimi.dsi.fastutil", "com.sk89q.worldedit.bukkit.fastutil")
        relocate("net.royawesome.jlibnoise", "com.sk89q.worldedit.jlibnoise")
        relocate("com.github.Anon8281.universalScheduler", "com.sk89q.worldedit.bukkit.universalScheduler")
    }
    project.project(":worldedit-bukkit:adapters").subprojects.forEach {
        dependencies {
            include(dependency("${it.group}:${it.name}"))
        }
        minimize {
            exclude(dependency("${it.group}:${it.name}"))
        }
    }
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}

configure<PublishingExtension> {
    publications.named<MavenPublication>("maven") {
        from(components["java"])
    }
}
