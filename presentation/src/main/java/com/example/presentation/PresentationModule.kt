package com.example.presentation

import com.example.presentation.features.auth.register.RegisterViewModel
import com.example.presentation.features.auth.verify.VerifyViewModel
import com.example.presentation.features.cart.CartViewModel
import com.example.presentation.features.product.details.ProductDetailsViewModel
import com.example.presentation.features.product.list.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {


    viewModel {
        RegisterViewModel(get(), get())
    }
    viewModel {
        VerifyViewModel(get())
    }
    viewModel {
        ProductViewModel(get(), get(), get(),get())
    }
    viewModel {
        CartViewModel(get(),get(),get(),get())
    }
    viewModel { ProductDetailsViewModel(get(),get(),get()) }
}