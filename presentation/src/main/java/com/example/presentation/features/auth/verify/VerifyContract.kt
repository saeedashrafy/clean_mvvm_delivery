package com.example.presentation.features.auth.verify


internal enum class ValidationError {
    INVALID_CODE,
    SHORT_CODE
}

internal sealed class VerifyPartialStateChange {
    abstract fun reduce(state: ViewState): ViewState

    data class ErrorsChanged(val errors: Set<ValidationError>) : VerifyPartialStateChange() {
        override fun reduce(state: ViewState): ViewState {
            return state.copy(errors = errors)
        }
    }


    data class TimerChanged(val time: Int) : VerifyPartialStateChange() {
        override fun reduce(state: ViewState): ViewState {
            return state.copy(timer = time)
        }

    }

    sealed class SubmitCode : VerifyPartialStateChange() {

        object Loading : SubmitCode()
        object SubmitCodeSuccess : SubmitCode()
        data class SubmitCodeFailure(val throwable: Throwable) : SubmitCode()


        override fun reduce(state: ViewState): ViewState {
            return when (this) {
                is Loading -> state.copy(isLoading = true)
                is SubmitCodeSuccess -> state.copy(isLoading = false)
                is SubmitCodeFailure -> state.copy(isLoading = false)
            }
        }
    }


}

internal sealed class ViewIntent {

    data class SubmitCode(val code: String?) : ViewIntent()
    object RetrySendCode : ViewIntent()
    data class CodeChanged(val code: String?) : ViewIntent()
}

internal data class ViewState(
        val errors: Set<ValidationError>,
        val isLoading: Boolean,
        val timer: Int
) {
    companion object {
        fun initial() = ViewState(errors = emptySet(), isLoading = false, timer = 20)
    }

}

internal sealed class SingleEvent {
    data class SubmitCodeFailure(val throwable: Throwable) : SingleEvent()
    object SubmitCodeSuccess : SingleEvent()

}