plugins {
    id("android-library-convention")
}

android {
    namespace = "com.skilltest.shared"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
