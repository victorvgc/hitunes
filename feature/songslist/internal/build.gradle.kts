plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.ui"
}

dependencies {
    implementation(project(":core:presentation"))
    implementation(project(":core:util"))
    implementation(project(":design"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))

    implementation(libs.koin)
    implementation(libs.koin.compose)
}
