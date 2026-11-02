plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.internal"
}

dependencies {
    implementation(project(":design"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":core:presentation"))
}
