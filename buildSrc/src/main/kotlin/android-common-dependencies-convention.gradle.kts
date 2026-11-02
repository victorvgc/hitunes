// Apply after the Android application or library plugin. Shared by both module types.
dependencies {
    add("implementation", versionCatalog.findLibrary("androidx-core-ktx").get())
    add("implementation", versionCatalog.findLibrary("androidx-lifecycle-runtime-ktx").get())
    add("implementation", versionCatalog.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
    add("implementation", versionCatalog.findLibrary("timber").get())

    add("testImplementation", versionCatalog.findLibrary("junit").get())
    add("testImplementation", versionCatalog.findLibrary("kotlinx-coroutines-test").get())
    add("androidTestImplementation", versionCatalog.findLibrary("androidx-junit").get())
    add("androidTestImplementation", versionCatalog.findLibrary("androidx-espresso-core").get())
}
