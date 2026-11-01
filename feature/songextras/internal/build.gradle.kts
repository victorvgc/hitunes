plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.android.roborazzi)
}

roborazzi {
    outputDir.set(file("src/test/snapshots"))
}

android {
    namespace = "com.skilltest.internal"
}

dependencies {
    implementation(project(":design"))
    implementation(project(":domain:model"))
    implementation(project(":core:presentation"))
    implementation(libs.koin)
    implementation(libs.koin.compose)
}
