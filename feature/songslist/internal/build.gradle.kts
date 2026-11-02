plugins {
    id("android-library-compose-convention")
}

android {
    namespace = "com.skilltest.ui"
}

dependencies {
    implementation(project(":core:presentation"))
    implementation(project(":design"))
    implementation(project(":navigation"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":feature:songextras:shared"))
}
