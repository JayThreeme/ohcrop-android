pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven(uri("https://jitpack.io"))//charts
        maven(uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/dev"))//charts .kt
        maven(uri("https://maven.pkg.jetbrains.space/data2viz/p/maven/public"))//charts .kt


    }
}

rootProject.name = "OHCrop"
include(":app")
