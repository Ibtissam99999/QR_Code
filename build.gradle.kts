// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.dokka") version "1.8.20"

}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

buildscript {
    repositories {
        mavenCentral()
    }

}


