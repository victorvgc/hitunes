plugins {
    id("com.android.library")
    id("android-common-dependencies-convention")
}

android {
    compileSdk = AndroidAppConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidAppConfig.MIN_SDK
        testInstrumentationRunner = AndroidAppConfig.TEST_INSTRUMENTATION_RUNNER
    }

    compileOptions {
        sourceCompatibility = AndroidAppConfig.JAVA_VERSION
        targetCompatibility = AndroidAppConfig.JAVA_VERSION
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}
