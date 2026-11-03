plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.songextras.shared"
}

dependencies {
    api(project(":domain:model"))
    implementation(project(":feature:songextras:internal"))
    implementation(project(":core:presentation"))
}
