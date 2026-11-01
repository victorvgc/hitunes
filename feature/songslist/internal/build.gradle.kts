plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.ui"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":design"))

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
}
