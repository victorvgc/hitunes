plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.skilltest.navigation"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.compose.nav)
    implementation(libs.serialization)
}
