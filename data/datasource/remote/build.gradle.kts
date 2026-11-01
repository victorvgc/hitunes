plugins {
    id("android-library-convention")
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.skilltest.data.remote"
    buildFeatures.buildConfig = true
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    implementation(project(":data:datasource"))
    implementation(libs.koin.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
}
