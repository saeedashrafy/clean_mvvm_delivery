package com.example.presentation.features.auth.register


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.flatMapFirst
import com.example.market.core.withLatestFrom
import com.example.market.domain.entity.AppInit
import com.example.market.domain.entity.NetworkError
import com.example.market.domain.entity.Register
import com.example.market.domain.useCase.InitAppUseCase


import com.example.market.domain.useCase.RegisterNumberUseCase


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


@ExperimentalCoroutinesApi
internal class RegisterViewModel(
        private val registerNumberUseCase: RegisterNumberUseCase,
        private val initAppUseCase: InitAppUseCase
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<SingleEvent>(extraBufferCapacity = 64)
    private val _intentChannel = MutableSharedFlow<ViewIntent>(extraBufferCapacity = 64)
    val viewState: StateFlow<ViewState>
    val singleEvent: Flow<SingleEvent> get() = _eventFlow

    init {
        val initialVS = ViewState.initial()
        viewState = merge(_intentChannel.filterIsInstance<ViewIntent.Initial>().take(1),
                _intentChannel.filterNot { it is ViewIntent.Initial })
                .toPartialStateChangesFlow()
                .sendSingleEvent()
                .scan(initialVS) { state, change -> change.reduce(state) }
                .catch { }
                .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)

    }


    suspend fun processIntent(intent: ViewIntent) = _intentChannel.emit(intent)
    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is PartialStateChange.ErrorsChanged -> return@onEach
                PartialStateChange.RegisterPhoneNumber.Loading -> return@onEach
                is PartialStateChange.RegisterPhoneNumber.RegisterPhoneNumberSuccess ->{if(change.hasPassword)   SingleEvent.NavigatePassword else  SingleEvent.NavigateVerify  }
                is PartialStateChange.RegisterPhoneNumber.RegisterPhoneNumberFailure -> return@onEach
                is PartialStateChange.InitialApp.Success -> return@onEach
                is PartialStateChange.InitialApp.Error -> return@onEach
                is PartialStateChange.InitialApp.Loading -> return@onEach
            }
            _eventFlow.emit(event)
        }
    }

    private fun Flow<ViewIntent>.toPartialStateChangesFlow(): Flow<PartialStateChange> {
        val phoneNumberErrors = filterIsInstance<ViewIntent.PhoneNumberChanged>()
                .map {
                    it.phoneNumber
                }
                .map { validatePhoneNumber(it) to it }


        val userFormFlow = phoneNumberErrors.map { t ->
            UserForm(
                    errors = t.first,
                    phoneNumber = t.second ?: ""

            )

        }.shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed()
        )


        val addUserChanges = filterIsInstance<ViewIntent.Submit>()
                .withLatestFrom(userFormFlow) { _, userForm -> userForm }
                .filter { it.errors.isEmpty() }
                .map { it.phoneNumber }
                .flatMapFirst { phoneNumber ->
                    registerNumberUseCase(phoneNumber = phoneNumber)
                            .map {

                                it.fold({ failure ->
                                    PartialStateChange.RegisterPhoneNumber.RegisterPhoneNumberFailure(
                                            failure
                                    )
                                },
                                        { authenticate ->
                                            PartialStateChange.RegisterPhoneNumber.RegisterPhoneNumberSuccess(
                                                    authenticate.hasPassword
                                            )

                                        }).let {
                                    return@map it as PartialStateChange
                                }
                            }
                            .onStart { emit(PartialStateChange.RegisterPhoneNumber.Loading) }
                            .catch {
                                emit(
                                        PartialStateChange.RegisterPhoneNumber.RegisterPhoneNumberFailure(
                                                NetworkError
                                        )
                                )
                            }
                }
        val initAppChanges = filterIsInstance<ViewIntent.Initial>().flatMapConcat {
            initAppUseCase(it.appSpecification)
        }.map {
            it.fold({ failure ->
                PartialStateChange.InitialApp.Error(failure = failure)
            }, { appInit ->
                PartialStateChange.InitialApp.Success(appInit = appInit)
            }).let {
                return@map it as PartialStateChange.InitialApp
            }

        }.onStart {
            emit(PartialStateChange.InitialApp.Loading)
        }

        val retryInitAppChanges = filterIsInstance<ViewIntent.RetryInit>().map {
            initAppUseCase(it.appSpecification)
        }.flatMapMerge {
            it.map {
                it.fold({ failure ->
                    PartialStateChange.InitialApp.Error(failure = failure)
                }, { appInit ->
                    PartialStateChange.InitialApp.Success(appInit = appInit)
                }).let {
                    return@map it as PartialStateChange.InitialApp
                }

            }.onStart {
                emit(PartialStateChange.InitialApp.Loading)
            }

        }


        return merge(userFormFlow
                .map { it.errors }
                .map { PartialStateChange.ErrorsChanged(it) },
                addUserChanges,
                initAppChanges,
                retryInitAppChanges
        )

    }

    private companion object {
        const val MIN_LENGTH_PHONE_NUMBER = 2


        private fun validatePhoneNumber(phoneNumber: String?): Set<ValidationError> {
            val errors = mutableSetOf<ValidationError>()

            if (phoneNumber != null) {

                if (phoneNumber?.length >= MIN_LENGTH_PHONE_NUMBER && !phoneNumber.startsWith("09")) {
                    errors += ValidationError.INVALID_PHONE_NUMBER
                }
            }
            if (phoneNumber == null || phoneNumber.length < 11) {
                errors += ValidationError.TOO_SHORT_NUMBER
            }

            // more validation here

            return errors
        }

        private data class UserForm(
                val errors: Set<ValidationError>,
                val phoneNumber: String
        )

    }
}