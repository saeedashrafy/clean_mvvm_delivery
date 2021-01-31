import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    androidLib
    kotlinAndroid
    kotlin("kapt")
}

android {
    compileSdkVersion(appConfig.compileSdkVersion)
    buildToolsVersion(appConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(appConfig.minSdkVersion)
        targetSdkVersion(appConfig.targetSdkVersion)
        versionCode = appConfig.versionCode
        versionName = appConfig.versionName

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
    implementation(domain)
    implementation(core)

    implementation(deps.androidx.appCompat)
    implementation(deps.androidx.coreKtx)

    implementation(deps.lifecycle.viewModelKtx)
    implementation(deps.lifecycle.runtimeKtx)


    implementation(deps.androidx.constraintLayout)
    implementation(deps.androidx.material)



    implementation(deps.jetbrains.coroutinesCore)

    implementation(deps.koin.androidXViewModel)

    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")


    implementation(deps.squareup.loggingInterceptor)

    implementation(deps.bumptech.glide)
    kapt(deps.bumptech.glideAnnotation)

    implementation(deps.akexorcist.cornerProgress)



    api(deps.androidx.navigationFragment)

}
