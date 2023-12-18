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
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

gradle.extra.apply {
    set("androidxMediaModulePrefix", "media-")
}
apply(from = file("/home/david/workspace/media/core_settings.gradle"))

rootProject.name = "AndroidKaraokePlayer"
include(":app")
