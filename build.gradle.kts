// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // These plugins are supplied by buildSrc using the version catalog.
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
    id("com.android.library") apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("io.github.takahirom.roborazzi") apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    id("org.jetbrains.kotlin.jvm") apply false
}
