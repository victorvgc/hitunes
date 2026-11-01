import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

// Compose defaults can be used by either an application or a library.
pluginManager.withPlugin("com.android.application") {
    extensions.configure<ApplicationExtension> {
        buildFeatures.compose = true
    }
}
pluginManager.withPlugin("com.android.library") {
    extensions.configure<LibraryExtension> {
        buildFeatures.compose = true
    }
}

dependencies {
    add("implementation", platform(versionCatalog.findLibrary("androidx-compose-bom").get()))
    add(
        "androidTestImplementation",
        platform(versionCatalog.findLibrary("androidx-compose-bom").get())
    )
    add("implementation", versionCatalog.findBundle("compose").get())
    add("implementation", versionCatalog.findLibrary("androidx-lifecycle-compose-ktx").get())

    add("testImplementation", versionCatalog.findBundle("roborazzi").get())
    add(
        "androidTestImplementation",
        versionCatalog.findLibrary("androidx-compose-ui-test-junit4").get()
    )
    add(
        "debugImplementation",
        versionCatalog.findLibrary("androidx-compose-ui-test-manifest").get()
    )
    add("debugImplementation", versionCatalog.findLibrary("androidx-compose-ui-tooling").get())
}
