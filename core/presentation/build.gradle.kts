plugins {
    id("android-library-convention")
}

android {
    namespace = "com.skilltest.core.presentation"
}

dependencies {
    api(libs.androidx.lifecycle.viewmodel.ktx)
}
