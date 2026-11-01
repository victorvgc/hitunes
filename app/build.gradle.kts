plugins {
    id("com.android.application")
    id("android-common-dependencies-convention")
    id("android-compose-convention")
    alias(libs.plugins.secrets)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.skilltest.hitunes"
    compileSdk = AndroidAppConfig.COMPILE_SDK

    defaultConfig {
        applicationId = AndroidAppConfig.APPLICATION_ID
        minSdk = AndroidAppConfig.MIN_SDK
        targetSdk = AndroidAppConfig.TARGET_SDK
        versionCode = AndroidAppConfig.VERSION_CODE
        versionName = AndroidAppConfig.VERSION_NAME

        testInstrumentationRunner = AndroidAppConfig.TEST_INSTRUMENTATION_RUNNER
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
        sourceCompatibility = AndroidAppConfig.JAVA_VERSION
        targetCompatibility = AndroidAppConfig.JAVA_VERSION
    }
    buildFeatures {
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
    implementation(project(":design"))
    implementation(project(":navigation"))
    implementation(project(":feature:songslist:shared"))
    implementation(project(":data:datasource:remote"))
    implementation(project(":data:datasource:local"))
    implementation(project(":data:repository"))

    implementation(libs.androidx.compose.nav)
    implementation(libs.serialization)

    implementation(libs.androidx.splash)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
}
