plugins {
    id("kotlin-library-convention")
}

dependencies {
    api(project(":domain:repository"))
    implementation(libs.koin.core)
}
