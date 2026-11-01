plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.android.roborazzi)
}

android {
    namespace = "com.skilltest.core.util"
}

dependencies {
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.espresso.core)
}
