plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.9.10"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    val ktorVersion = "2.3.2"
    val coroutinesVersion = "1.7.3"
    val koinVersion = "3.4.3"
    val multiplatformSettingsVersion = "1.0.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation ("io.insert-koin:koin-core:$koinVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                implementation("com.russhwolf:multiplatform-settings:$multiplatformSettingsVersion")
                implementation("com.russhwolf:multiplatform-settings-coroutines:$multiplatformSettingsVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

                implementation ("androidx.work:work-runtime:2.8.1")
                implementation ("androidx.work:work-runtime-ktx:2.8.1")

                implementation ("io.insert-koin:koin-android:$koinVersion")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")

                implementation ("io.insert-koin:koin-core:$koinVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
    }
}

android {
    namespace = "com.unsplash.android"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
    binaries.all {
        binaryOptions["memoryModel"] = "experimental"
    }
}
