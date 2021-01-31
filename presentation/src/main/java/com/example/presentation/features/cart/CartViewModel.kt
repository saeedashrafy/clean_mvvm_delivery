package com.example.presentation.features.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.flatMapFirst
import com.example.market.domain.entity.CartFood
import com.example.market.domain.useCase.AddFoodToCartUseCase
import com.example.market.domain.useCase.GetAllCartUseCase
import com.example.market.domain.useCase.GetPreOrderUseCase
import com.example.market.domain.useCase.RemoveFoodFromCartUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

internal class CartViewModel(
    private val getPreOrderUseCase: GetPreOrderUseCase,
    private val getAllCartUseCase: GetAllCartUseCase,
    private val addFoodToCartUseCase: AddFoodToCartUseCase,
    private val removeFoodFromCartUseCase: RemoveFoodFromCartUseCase
) : ViewModel() {
    private val _intentFLow = MutableSharedFlow<ViewIntent>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val viewState: StateFlow<ViewState>


    init {
        val initialVS = ViewState.initialize()

        viewState = merge(_intentFLow.filterIsInstance<ViewIntent>().take(1),
            _intentFLow.filterNot { it is ViewIntent.Init })
            .toPartialChanges()
            .scan(initialVS) { viewState, change -> change.reduce(viewState) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)

    }

    suspend fun processIntent(viewIntent: ViewIntent) = _intentFLow.emit(viewIntent)

    private fun Flow<ViewIntent>.toPartialChanges(): Flow<PartialStateChange> {


        val initFlow: Flow<PartialStateChange> =
           getAllCartUseCase()
                .flatMapConcat {
                    Log.d("TAG",it.toString())
                    getPreOrderUseCase(it)
                }.map {
                    it.fold({ failure -> PartialStateChange.Init.Failure(failure) }, { order ->
                        PartialStateChange.Init.Success(cartRows = order.orderItems?.filter { it.count > 0 }
                            ?.map { orderItem ->
                                CartRow(
                                    orderItem.foodId,
                                    orderItem.title,
                                    orderItem.count,
                                    orderItem.eachFoodItemPrice,
                                    orderItem.totalFoodItemsPrice,
                                    orderItem.image
                                )
                            }, factor = order.factor)
                    }).let {
                        it as PartialStateChange
                    }
                }.onStart {
                    emit(PartialStateChange.Init.Loading)
                }


        val addFoodToCartChanges = filterIsInstance<ViewIntent.IncreaseCount>()
            .map {
                it.cartRow.toDomain()
            }
            .flatMapConcat { cartFood ->
                flow { addFoodToCartUseCase.invoke(cartFood).let { emit(it) } }
            }
            .map { PartialStateChange.AddFoodToCart.Success as PartialStateChange }

        val removeFoodCartChanges = filterIsInstance<ViewIntent.DecreaseCount>()
            .map { it.cartRow.toDomain() }
            .flatMapConcat { cartFood ->
                flow { removeFoodFromCartUseCase.invoke(cartFood).let { emit(it) } }
            }
            .map { PartialStateChange.RemoveFoodFromCart.Success as PartialStateChange }


        return merge(
            addFoodToCartChanges,
                removeFoodCartChanges,
                filterIsInstance<ViewIntent.Init>().flatMapConcat { initFlow },
                filterIsInstance<ViewIntent.Refresh>().flatMapMerge { initFlow }
        )
    }
}