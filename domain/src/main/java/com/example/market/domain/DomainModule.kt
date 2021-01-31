package com.example.market.domain

import com.example.market.domain.useCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val domainModule = module {


    factory { RegisterNumberUseCase(get()) }
    factory { VerifyCodeUseCase(get()) }
    factory { UploadImageUseCase(get()) }
    factory { GetCategoryProductsUseCase(get()) }
    factory { AddFoodToCartUseCase(get()) }
    factory { GetAllCartUseCase(get()) }
    factory { RemoveFoodFromCartUseCase(get()) }
    factory { InitAppUseCase(get()) }
    factory { CheckIsLoginUseCase(get()) }
    factory { GetPreOrderUseCase(get()) }
}