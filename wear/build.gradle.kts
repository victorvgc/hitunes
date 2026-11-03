plugins {
    id("com.android.application")
    id("android-common-dependencies-convention")
    id("android-compose-convention")
}

android {
    namespace = "com.skilltest.hitunes.wear"
    compileSdk = AndroidAppConfig.COMPILE_SDK

    defaultConfig {
        // Same id as the phone app so the Wearable Data Layer pairs them.
        applicationId = AndroidAppConfig.APPLICATION_ID
        minSdk = AndroidAppConfig.WEAR_MIN_SDK
        targetSdk = AndroidAppConfig.TARGET_SDK
        versionCode = AndroidAppConfig.VERSION_CODE
        versionName = AndroidAppConfig.VERSION_NAME

        testInstrumentationRunner = AndroidAppConfig.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = AndroidAppConfig.JAVA_VERSION
        targetCompatibility = AndroidAppConfig.JAVA_VERSION
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core:wearsync"))
    implementation(project(":design"))

    implementation(libs.wear.compose.material3)
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.ui.tooling)
    implementation(libs.kotlinx.coroutines.play.services)
}
