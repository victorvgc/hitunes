plugins {
    id("kotlin-library-convention")
}

dependencies {
    api(project(":domain:repository"))
    implementation(project(":data:datasource"))
    implementation(libs.koin.core)
}
