plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.shared"
}

dependencies {
    implementation(project(":feature:songslist:internal"))
    implementation(project(":navigation"))

    implementation(libs.koin.core)
}
