plugins {
    id("android-library-convention")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.skilltest.navigation"

    buildFeatures.compose = true
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.compose.nav)
    implementation(libs.serialization)
}
