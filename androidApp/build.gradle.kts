plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.unsplash.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.unsplash.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + project.buildDir.absolutePath + "/compose_metrics")
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination="  + project.buildDir.absolutePath + "/compose_metrics")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.ui:ui-tooling:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation("androidx.compose.foundation:foundation:1.5.3")

    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("com.google.accompanist:accompanist-placeholder-material:0.30.1")
    implementation("com.google.accompanist:accompanist-placeholder:0.30.1")
    implementation("com.google.accompanist:accompanist-pager:0.30.1")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.navigation:navigation-compose:2.7.0")

    implementation("io.insert-koin:koin-androidx-compose:3.4.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    implementation ("com.google.maps.android:maps-compose:2.12.0")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
}