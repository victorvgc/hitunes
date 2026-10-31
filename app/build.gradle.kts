plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets)
    kotlin("plugin.serialization") version "2.0.21"
}

val versionMajor = 0
val versionMinor = 0
val versionPatch = 1

android {
    namespace = "com.skilltest.hitunes"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.skilltest.hitunes"
        minSdk = 28
        targetSdk = 37
        versionCode = generateVersionCode()
        versionName = generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false


        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":design"))
    implementation(project(":navigation"))
    implementation(project(":feature:songslist:shared"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.junit)
    testImplementation(libs.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.timber)
    implementation(libs.androidx.compose.nav)
    implementation(libs.serialization)

    implementation(libs.androidx.splash)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
}

private fun generateVersionCode() =
    versionMajor * 10000 + versionMinor * 100 + versionPatch

private fun generateVersionName() =
    "%d.%02d.%02d".format(versionMajor, versionMinor, versionPatch)
