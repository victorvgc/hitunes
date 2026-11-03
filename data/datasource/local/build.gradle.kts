plugins {
    id("android-library-convention")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.skilltest.data.local"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(project(":data:datasource"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.robolectric)
}
