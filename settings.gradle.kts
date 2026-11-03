pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HiTunes"
include(":app")
include(":core")
include(":core:presentation")
include(":feature:songslist")
include(":feature:songslist:shared")
include(":feature:songslist:internal")
include(":design")
include(":navigation")
include(":feature:songextras")
include(":feature:songextras:internal")
include(":feature:songextras:shared")
include(":domain")
include(":data")
include(":domain:model")
include(":domain:repository")
include(":domain:usecase")
include(":data:datasource")
include(":data:datasource:remote")
include(":data:datasource:local")
include(":data:repository")
include(":feature:songplayer:internal")
include(":feature:songplayer:shared")
