plugins {
    id("android-library-compose-convention")
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
