plugins {
    kotlin("jvm") version "2.3.0"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.gradleup.shadow") version "9.3.1"
}

javafx {
    version = "22.0.1"
    modules("javafx.controls")
}

group = "dev.blackoutburst.bogel"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.4.1"

val lwjglModules = listOf("lwjgl", "lwjgl-assimp", "lwjgl-glfw", "lwjgl-opengl", "lwjgl-stb")

val nativesClassifiers = listOf(
    "natives-linux",
    "natives-linux-arm32",
    "natives-linux-arm64",
    "natives-linux-ppc64le",
    "natives-linux-riscv64",
    "natives-macos",
    "natives-macos-arm64",
    "natives-windows",
    "natives-windows-x86",
    "natives-windows-arm64"
)

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    lwjglModules.forEach { module ->
        implementation("org.lwjgl:$module")
    }

    lwjglModules.forEach { module ->
        nativesClassifiers.forEach { classifier ->
            implementation("org.lwjgl:$module:$lwjglVersion:$classifier")
        }
    }

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}