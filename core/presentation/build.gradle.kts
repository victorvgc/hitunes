plugins {
    id("android-library-convention")
}

android {
    namespace = "com.skilltest.core.presentation"
}

dependencies {
    // BaseViewModel exposes DispatcherProvider and ViewModel in its public API.
    api(project(":core:util"))
    api(libs.androidx.lifecycle.viewmodel.ktx)
}
