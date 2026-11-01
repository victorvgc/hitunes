import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = AndroidAppConfig.JAVA_VERSION
    targetCompatibility = AndroidAppConfig.JAVA_VERSION
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(AndroidAppConfig.JAVA_VERSION.toString())
    }
}

dependencies {
    testImplementation(versionCatalog.findLibrary("junit").get())
    testImplementation(versionCatalog.findLibrary("kotlinx-coroutines-test").get())
}

tasks.withType<Test>().configureEach {
    useJUnit()
}
