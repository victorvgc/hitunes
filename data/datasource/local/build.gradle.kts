plugins {
    id("kotlin-library-convention")
}

dependencies {
    implementation(project(":data:datasource"))
    implementation(libs.koin.core)
}
