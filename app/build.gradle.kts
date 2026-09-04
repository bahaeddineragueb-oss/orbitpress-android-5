plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.askinz.publisher"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.askinz.publisher.v5"
    minSdk = 26
    targetSdk = 35
    versionCode = 10
    versionName = "5.0.0"
  }

  signingConfigs {
    create("stableRelease") {
      storeFile = file("../signing/orbitpress-v5-release.jks")
      storePassword = "OrbitPress5Store2026!"
      keyAlias = "orbitpress-release"
      keyPassword = "OrbitPress5Key2026!"
    }
  }
  buildTypes {
    getByName("release") {
      signingConfig = signingConfigs.getByName("stableRelease")
    }
  }

  buildFeatures { buildConfig = true }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin { jvmToolchain(17) }

dependencies {
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  testImplementation("junit:junit:4.13.2")
  testImplementation("org.json:json:20240303")
}
