plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.compose")
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
      val keystorePath = System.getenv("ORBITPRESS_KEYSTORE_PATH") ?: "../signing/orbitpress-v5-release.jks"
      val keystoreFile = file(keystorePath)
      if (keystoreFile.exists()) {
        storeFile = keystoreFile
        storePassword = System.getenv("ORBITPRESS_STORE_PASSWORD") ?: "OrbitPress5Store2026!"
        keyAlias = System.getenv("ORBITPRESS_KEY_ALIAS") ?: "orbitpress-release"
        keyPassword = System.getenv("ORBITPRESS_KEY_PASSWORD") ?: "OrbitPress5Key2026!"
      }
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      val releaseSigning = signingConfigs.findByName("stableRelease")
      if (releaseSigning?.storeFile?.exists() == true) {
        signingConfig = releaseSigning
      }
    }
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin { jvmToolchain(17) }

dependencies {
  val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
  implementation(composeBom)
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.material:material-icons-extended")
  implementation("androidx.activity:activity-compose:1.9.3")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  implementation("androidx.webkit:webkit:1.12.1")

  testImplementation("junit:junit:4.13.2")
  testImplementation("org.json:json:20240303")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}
