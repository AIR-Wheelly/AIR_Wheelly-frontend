plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

kotlin {
    jvmToolchain(8)
}
dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":ws")))

    //Google oAuth
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // Google Sign-In dependencies
    implementation("com.google.android.gms:play-services-auth:20.4.1")
    implementation("com.google.android.gms:play-services-identity:17.0.0")
}