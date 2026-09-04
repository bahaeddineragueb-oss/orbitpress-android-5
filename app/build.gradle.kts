plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.askinz.publisher"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.askinz.publisher"
    minSdk = 26
    targetSdk = 35
    versionCode = 9
    versionName = "5.0.0"
  }

  buildTypes {
    getByName("release") {
      // Public test build: use the CI-generated debug key so Android can install it.
      // Production distribution should replace this with a private release keystore.
      signingConfig = signingConfigs.getByName("debug")
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
