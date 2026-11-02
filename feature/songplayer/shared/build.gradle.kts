plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.songplayer.shared"
}

dependencies {
    api(project(":feature:songplayer:internal"))
    implementation(project(":design"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":core:presentation"))

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
}
