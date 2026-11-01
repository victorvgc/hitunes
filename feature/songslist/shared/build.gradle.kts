plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.shared"
}

dependencies {
    implementation(project(":feature:songslist:internal"))

    implementation(libs.koin)
    implementation(libs.koin.compose)
}
