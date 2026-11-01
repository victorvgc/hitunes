plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.android.roborazzi)
}

roborazzi {
    outputDir.set(file("src/test/snapshots"))
}

android {
    namespace = "com.skilltest.design"
}

dependencies {
    implementation(libs.material3)

    implementation(libs.coil)
    implementation(libs.coil.okhttp)
    implementation(libs.kotlin.collections)
}
