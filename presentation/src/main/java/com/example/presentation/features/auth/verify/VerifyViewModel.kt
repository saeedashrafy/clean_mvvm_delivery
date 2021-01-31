package com.example.presentation.features.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.flatMapFirst
import com.example.market.domain.entity.Confirm
import com.example.market.domain.useCase.VerifyCodeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
internal class VerifyViewModel(private val verifyCodeUseCase: VerifyCodeUseCase) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<SingleEvent>(extraBufferCapacity = 64)
    private val _intentChannel = MutableSharedFlow<ViewIntent>(extraBufferCapacity = 64)
    val viewState: StateFlow<ViewState>
    val singleEvent: Flow<SingleEvent> get() = _eventFlow

    init {
        val initialVS = ViewState.initial()

        viewState = _intentChannel
                .toPartialStateChangesFlow()
                .sendSingleEvent()
                .scan(initialVS) { oldState, newPartialState -> newPartialState.reduce(oldState) }
                .catch { }
                .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)

    }

    suspend fun processIntent(intent: ViewIntent) = _intentChannel.emit(intent)


    private fun Flow<VerifyPartialStateChange>.sendSingleEvent(): Flow<VerifyPartialStateChange> {

        return onEach { change ->
            val event = when (change) {
                VerifyPartialStateChange.SubmitCode.SubmitCodeSuccess -> SingleEvent.SubmitCodeSuccess
                VerifyPartialStateChange.SubmitCode.Loading -> return@onEach
                is VerifyPartialStateChange.SubmitCode.SubmitCodeFailure -> SingleEvent.SubmitCodeFailure(change.throwable)
                is VerifyPartialStateChange.ErrorsChanged -> return@onEach
                is VerifyPartialStateChange.TimerChanged -> return@onEach
            }
            _eventFlow.emit(event)

        }
    }

    private fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<VerifyPartialStateChange> {
        val countTimerFlow = timerFlow()

        /* val codeErrors = filterIsInstance<ViewIntent.CodeChanged>()
                 .map { it.code }
                 .map { validateCode(it) to it }
         val codeFormFlow = codeErrors.map {
             VerifyForm(
                     error = it.first,
                     authentication = Authentication(it.second ?: "")
             )
         }
                 .shareIn(
                         scope = viewModelScope,
                         started = SharingStarted.WhileSubscribed()
                 )*/
        val retrySendCodeChanges = filterIsInstance<ViewIntent.RetrySendCode>()
                .flatMapFirst {
                    timerFlow()
                }


        val submitCodeChanges = filterIsInstance<ViewIntent.SubmitCode>()
                .map { it.code?.let { it1 -> Confirm(otp = it1) } }
                .flatMapFirst {
                    flow {
                        emit(it?.let { it1 -> verifyCodeUseCase(it1.otp) })
                    }
                            .map {
                                @Suppress("USELESS_CAST")
                                VerifyPartialStateChange.SubmitCode.SubmitCodeSuccess as VerifyPartialStateChange.SubmitCode
                            }
                            .onStart { emit(VerifyPartialStateChange.SubmitCode.Loading) }
                            .catch {
                                emit(VerifyPartialStateChange.SubmitCode.SubmitCodeFailure(it)
                                )
                            }
                }

        return merge(
                submitCodeChanges,
                countTimerFlow,
                retrySendCodeChanges
        )
    }

    companion object {

        const val MIN_LENGTH_CODE = 5

        private fun validateCode(code: String?): Set<ValidationError> {
            val errors = mutableSetOf<ValidationError>()


            if (code.isNullOrEmpty() || code.length < MIN_LENGTH_CODE) {
                errors += ValidationError.SHORT_CODE
            }
            return errors
        }


        private fun timerFlow() = (15 downTo 0).asFlow().onEach { delay(1000) }
                .map {
                    VerifyPartialStateChange.TimerChanged(it)
                }


        data class VerifyForm(
                val error: Set<ValidationError>,
                val confirm: Confirm
        )

    }
}


