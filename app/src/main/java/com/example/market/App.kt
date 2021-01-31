package com.example.market

import android.app.Application
import com.example.market.core.coreModule
import com.example.market.data.dataModule
import com.example.market.domain.domainModule
import com.example.presentation.presentationModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalTime
class App : Application() {


    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                coreModule, dataModule,
                presentationModule, domainModule
            )
        }

    }

}