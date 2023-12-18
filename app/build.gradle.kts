plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.android.karaokeplayer"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.android.karaokeplayer"
    minSdk = 23
    targetSdk = 34
    versionCode = 2
    versionName = "2"
    vectorDrawables {
      useSupportLibrary = true
    }

  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  ndkVersion = "26.1.10909125"
}

var ktor_version = "2.3.7"
dependencies {

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.tv:tv-foundation:1.0.0-alpha10")
  implementation("androidx.tv:tv-material:1.0.0-alpha10")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
  implementation("androidx.activity:activity-compose:1.8.1")

  implementation(project(":media-lib-exoplayer"))
  implementation(project(":media-lib-exoplayer-dash"))
  implementation(project(":media-lib-ui"))
  implementation(project(":media-lib-exoplayer-smoothstreaming"))
  implementation(project(":media-lib-decoder-ffmpeg"))

  implementation("io.ktor:ktor-client-core:$ktor_version")
  implementation("io.ktor:ktor-client-cio:$ktor_version")
  implementation("io.ktor:ktor-client-websockets:$ktor_version")

  implementation("org.jmdns:jmdns:3.5.9")
  implementation("org.slf4j:slf4j-simple:2.0.6")

  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}