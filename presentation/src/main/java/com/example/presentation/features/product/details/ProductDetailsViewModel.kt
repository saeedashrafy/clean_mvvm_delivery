package com.example.presentation.features.product.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.domain.useCase.AddFoodToCartUseCase
import com.example.market.domain.useCase.GetAllCartUseCase

import com.example.market.domain.useCase.RemoveFoodFromCartUseCase
import com.example.presentation.features.product.details.PartialStateChanges.AddFoodToCart.Success
import com.example.presentation.features.product.details.PartialStateChanges.RemoveFoodFromCart
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

internal class ProductDetailsViewModel(private val addFoodToCartUseCase: AddFoodToCartUseCase,
                                       private val removeFoodFromCartUseCase: RemoveFoodFromCartUseCase,
                                       private val getAllCartUseCase: GetAllCartUseCase) : ViewModel() {

    private val _intentFlow = MutableSharedFlow<ViewIntent>(extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val stateFlow: StateFlow<ViewState>


    suspend fun processIntent(viewIntent: ViewIntent) = _intentFlow.emit(viewIntent)

    init {
        val initial = ViewState.initialize()
        stateFlow = merge(_intentFlow.filterIsInstance<ViewIntent.Init>().take(1),
                _intentFlow.filterNot { it is ViewIntent.Init })
                .toPartialChanges()
                .scan(initial) { state, changes -> changes.reduce(state) }
                .stateIn(viewModelScope, SharingStarted.Eagerly, initial)


    }

    private fun Flow<ViewIntent>.toPartialChanges(): Flow<PartialStateChanges> {

        val initChanges = filterIsInstance<ViewIntent.Init>().map { it.food }.flatMapMerge { food ->
            getAllCartUseCase().map { cartFoods ->
                val foundCart = cartFoods.find { cartFood -> cartFood.foodId == food!!.id }
                if (foundCart != null) PartialStateChanges.GetFood.Success(foundCart)
                else {
                    val foo = food!!.copy(count = 0)
                    PartialStateChanges.GetFood.Success(foo!!.toDomain())
                }
            }
        }


        val incrementChanges = filterIsInstance<ViewIntent.Increment>().map {
            it.food.toDomain()
        }.flatMapMerge { cartFood ->
            flow { emit(addFoodToCartUseCase.invoke(cartFood)) }
        }.map {
            PartialStateChanges.AddFoodToCart.Success as PartialStateChanges
        }
        val decrementChanges = filterIsInstance<ViewIntent.Decrement>().map {
            it.food.toDomain()
        }.flatMapMerge { cartFood ->
            flow { emit(removeFoodFromCartUseCase.invoke(cartFood)) }
        }.map {
            RemoveFoodFromCart.Success as PartialStateChanges
        }

        return merge(incrementChanges, decrementChanges, initChanges)

    }
}