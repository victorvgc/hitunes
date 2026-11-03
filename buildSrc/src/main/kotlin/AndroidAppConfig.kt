object AndroidAppConfig {
    private const val versionMajor = 0
    private const val versionMinor = 0
    private const val versionPatch = 1

    const val COMPILE_SDK = 37
    const val MIN_SDK = 28
    const val WEAR_MIN_SDK = 30
    const val TARGET_SDK = 37
    const val APPLICATION_ID = "com.skilltest.hitunes"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"

    val JAVA_VERSION = org.gradle.api.JavaVersion.VERSION_11

    val VERSION_CODE = generateVersionCode()
    val VERSION_NAME = generateVersionName()

    private fun generateVersionCode() =
        versionMajor * 10000 + versionMinor * 100 + versionPatch

    private fun generateVersionName() =
        "%d.%02d.%02d".format(versionMajor, versionMinor, versionPatch)
}
