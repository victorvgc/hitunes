// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // These plugins are supplied by buildSrc using the version catalog.
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
    id("com.android.library") apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.roborazzi) apply false
    alias(libs.plugins.secrets) apply false
    id("org.jetbrains.kotlin.jvm") apply false
}
