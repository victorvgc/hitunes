plugins {
    id("kotlin-library-convention")
}

dependencies {
    api(project(":domain:model"))
    api(libs.kotlinx.coroutines.core)
}
