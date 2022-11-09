import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.serdjuk"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven{
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
}


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.windows_x64)
                implementation("com.google.code.gson:gson:2.9.1")
//                implementation("io.ktor:ktor-server-core:2.1.3")
//                implementation("io.ktor:ktor-server-netty:2.1.3")
                implementation("io.ktor:ktor-client-core:2.1.3")
                implementation("io.ktor:ktor-client-cio:2.1.3")
                implementation("io.ktor:ktor-client-websockets:2.1.3")
                implementation("io.ktor:ktor-network-tls:2.1.3")
                implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CardCom"
            packageVersion = "1.0.0"
        }
    }
}
