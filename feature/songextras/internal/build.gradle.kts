plugins {
    id("android-library-convention")
}

android {
    namespace = "com.skilltest.internal"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}
