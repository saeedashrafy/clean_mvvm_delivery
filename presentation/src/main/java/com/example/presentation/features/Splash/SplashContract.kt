package com.example.presentation.features.Splash

import com.example.market.domain.entity.AppSpecification
import com.example.market.domain.entity.Failure


internal sealed class ViewIntent {

    data class Init( val appSpecification: AppSpecification) : ViewIntent()
}

internal data class ViewState(
        val isLoading: Boolean,
        val error: Failure?,
        val isLogin: Boolean
) {
    companion object {
        fun initialize() = ViewState(isLoading = false,error = null,isLogin = false)
    }
}

internal sealed class PartialStateChanges {
    abstract fun reduce(viewState: ViewState): ViewState
    sealed class InitApp : PartialStateChanges() {

        data class Success(val isLogin :Boolean) : InitApp()
        object Loading : InitApp()
        data class Failure(val failure: com.example.market.domain.entity.Failure) : InitApp()

        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
               is  Success -> viewState.copy(isLoading = false,isLogin= isLogin)
                Loading -> viewState.copy(isLoading = true)
                is Failure -> viewState.copy(isLoading = false, error = failure)
            }
        }
    }
}