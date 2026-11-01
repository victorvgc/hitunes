plugins {
    id("android-library-compose-convention")
    alias(libs.plugins.android.roborazzi)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.skilltest.core"

    buildFeatures.buildConfig = true
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)
}
