plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.songplayer.internal"
}

dependencies {
    implementation(project(":design"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    api(project(":core:presentation"))

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
}
