plugins {
    id("kotlin-library-convention")
}

dependencies {
    api(project(":domain:repository"))
    api(libs.kotlinx.coroutines.core)
}
