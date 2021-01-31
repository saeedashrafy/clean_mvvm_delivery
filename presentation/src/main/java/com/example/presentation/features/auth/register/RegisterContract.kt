package com.example.presentation.features.auth.register

import com.example.market.domain.entity.AppInit
import com.example.market.domain.entity.AppSpecification
import com.example.market.domain.entity.Failure


internal enum class ValidationError {
    INVALID_PHONE_NUMBER,
    TOO_SHORT_NUMBER,
}


internal data class ViewState(
        val errors: Set<ValidationError>,
        val initErrors: Failure?,
        val isLoading: Boolean,
        val data: AppInit?,
) {
    companion object {
        fun initial() = ViewState(
                errors = emptySet(),
                initErrors = null,
                isLoading = false,
                data = null
        )
    }
}

internal sealed class PartialStateChange {
    abstract fun reduce(viewState: ViewState): ViewState

    data class ErrorsChanged(val errors: Set<ValidationError>) : PartialStateChange() {
        override fun reduce(viewState: ViewState) = viewState.copy(errors = errors)
    }


    sealed class RegisterPhoneNumber : PartialStateChange() {
        object Loading : RegisterPhoneNumber()
        data class RegisterPhoneNumberSuccess(val hasPassword:Boolean) : RegisterPhoneNumber()
        data class RegisterPhoneNumberFailure(val failure: Failure) : RegisterPhoneNumber()


        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
                Loading -> viewState.copy(isLoading = true)
                is RegisterPhoneNumberSuccess -> viewState.copy(isLoading = false)
                is RegisterPhoneNumberFailure -> viewState.copy(isLoading = false)
            }
        }
    }

    sealed class InitialApp : PartialStateChange() {
        object Loading : InitialApp()
        data class Success(val appInit: AppInit) : InitialApp()
        data class Error(val failure: Failure) : InitialApp()

        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
                is Success -> viewState.copy(isLoading = false, data = appInit,initErrors = null)
                is Error -> viewState.copy(isLoading = false, initErrors = failure)
                Loading -> viewState.copy(isLoading = true,initErrors = null)
            }
        }
    }
}

internal sealed class ViewIntent {

    data class Initial(val appSpecification: AppSpecification) : ViewIntent()
    data class PhoneNumberChanged(val phoneNumber: String?) : ViewIntent()
    object Submit : ViewIntent()
    data class RetryInit(val appSpecification: AppSpecification) : ViewIntent()
}

internal sealed class SingleEvent {
    object NavigateVerify : SingleEvent()
    object NavigatePassword : SingleEvent()
    object Error : SingleEvent()

}