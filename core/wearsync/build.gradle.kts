plugins {
    id("android-library-convention")
}

android {
    namespace = "com.skilltest.core.wearsync"
}

dependencies {
    api(libs.play.services.wearable)
}
