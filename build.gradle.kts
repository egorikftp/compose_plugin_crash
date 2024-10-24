import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.1.0"
    // id("org.jetbrains.compose") version "1.6.10"
    id("org.jetbrains.compose") version "1.7.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
    dependencies {
        extensions.configure<IntelliJPlatformDependenciesExtension> {
            intellijIdeaCommunity("2024.1")
            instrumentationTools()
        }
    }
}

intellijPlatform {
    pluginConfiguration.ideaVersion {
        sinceBuild = "241"
        untilBuild = provider { null }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

dependencies {
    compileOnly(compose.desktop.currentOs)
    implementation(compose.desktop.common)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.material3)
}