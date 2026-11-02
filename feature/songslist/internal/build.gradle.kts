plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.android.roborazzi)
}

roborazzi {
    outputDir.set(file("src/test/snapshots"))
}

android {
    namespace = "com.skilltest.ui"
}

dependencies {
    implementation(project(":core:presentation"))
    implementation(project(":design"))
    implementation(project(":navigation"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":feature:songextras:shared"))

    implementation(libs.koin)
    implementation(libs.koin.compose)
}
