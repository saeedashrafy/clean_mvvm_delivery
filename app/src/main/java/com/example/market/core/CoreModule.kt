package com.example.market.core

import com.example.market.core.dispatchers.CoroutineDispatchers
import org.koin.dsl.module

val coreModule = module {
    single<CoroutineDispatchers> { CoroutineDispatchersImpl() }
}