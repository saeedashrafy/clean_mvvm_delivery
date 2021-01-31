plugins {
    androidApplication
    kotlinAndroid
    kotlin("android.extensions")
    kotlin("kapt")


}

android {


    packagingOptions {
        exclude("META-INF/notice.txt")
    }

    compileSdkVersion(appConfig.compileSdkVersion)
    buildToolsVersion(appConfig.buildToolsVersion)

    defaultConfig {
        applicationId = appConfig.applicationId
        minSdkVersion(appConfig.minSdkVersion)
        targetSdkVersion(appConfig.targetSdkVersion)
        versionCode = appConfig.versionCode
        versionName = appConfig.versionName
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
    buildFeatures { viewBinding = true }

}
dependencies {

    implementation(core)
    implementation(presentation)
    implementation(data)
    implementation(domain)

    implementation(deps.jetbrains.coroutinesAndroid)
    implementation(deps.koin.android)

    testImplementation(deps.test.junit)
  //  androidTestImplementation(deps.test.androidxJunit)
   // androidTestImplementation(deps.test.androidXSspresso)
}
