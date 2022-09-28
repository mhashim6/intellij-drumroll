plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.9.0"
}

group = "mhashim6.idea.plugin"
version = "2.0"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3")
    type.set("IC") // Target IDE Platform
    pluginName.set("Drum-Roll") //TODO
    plugins.set(listOf("java", "android"))
}

dependencies {
    implementation(fileTree("lib") {
        include("*.jar")
    })
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("223.*")
    }
    buildSearchableOptions {
        enabled = false
    }
    signPlugin {
        certificateChain.set(project.properties["CERTIFICATE_CHAIN"] as String)
        privateKey.set(project.properties["PRIVATE_KEY"] as String)
        password.set(project.properties["PRIVATE_KEY_PASSWORD"] as String)
    }

    publishPlugin {
        token.set(project.properties["intellijPublishToken"] as String)
    }
}
