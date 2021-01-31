package com.example.presentation.features.Splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.flatMapFirst
import com.example.market.core.withLatestFrom
import com.example.market.domain.useCase.CheckIsLoginUseCase
import com.example.market.domain.useCase.InitAppUseCase
import kotlinx.coroutines.flow.*

internal class SplashViewModel(
    private val checkIsLoginUseCase: CheckIsLoginUseCase
) : ViewModel() {
    private val _intentFlow = MutableSharedFlow<ViewIntent>(extraBufferCapacity = 64)
    val stateFlow: StateFlow<ViewState>
    suspend fun processIntents(viewIntent: ViewIntent) = _intentFlow.emit(viewIntent)

    init {
        val initialVS = ViewState.initialize()
        stateFlow = merge(
            _intentFlow.filterIsInstance<ViewIntent.Init>().take(1),
            _intentFlow.filterNot { it is ViewIntent.Init })
            .toPartialChangFlow()
            .scan(initialVS) { viewSate, changes -> changes.reduce(viewSate) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)
    }


    fun Flow<ViewIntent>.toPartialChangFlow(): Flow<PartialStateChanges> {

        return filterIsInstance<ViewIntent>().flatMapConcat {
            checkIsLoginUseCase()
        }.map {
            PartialStateChanges.InitApp.Success(it)
        }.onStart {
            PartialStateChanges.InitApp.Loading as PartialStateChanges.InitApp
        }


    }
}