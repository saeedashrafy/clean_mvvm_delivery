package com.example.presentation.features.cart

import com.example.market.domain.entity.CartFood
import com.example.market.domain.entity.Factor
import com.example.market.domain.entity.Failure


internal sealed class ViewIntent {

    object Init : ViewIntent()
    data class IncreaseCount(val cartRow: CartRow) : ViewIntent()
    data class DecreaseCount(val cartRow: CartRow) : ViewIntent()
    object Refresh :ViewIntent()
}

internal data class ViewState(
        val isLoading: Boolean,
        val rows: List<CartRow>?,
        val factor:Factor?,
        val error:Failure?
) {

    companion object {
        fun initialize(): ViewState = ViewState(isLoading = false, rows = emptyList(),error = null, factor = null)
    }
}

internal sealed class PartialStateChange {
    abstract fun reduce(viewState: ViewState): ViewState
    sealed class Init : PartialStateChange() {

        data class Success(val cartRows: List<CartRow>?, val factor: Factor?) : Init()
        object Loading : Init()
        data class Failure(val error:com.example.market.domain.entity.Failure?) : Init()

        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
                Loading -> viewState.copy(isLoading = true,error = null,)
                is Failure -> viewState.copy(isLoading = false,error = error,factor = null,rows = null)
                is Success -> viewState.copy(isLoading = false, rows = cartRows,factor=factor,error = null)
            }
        }
    }

    sealed class AddFoodToCart : PartialStateChange() {

        object Success : AddFoodToCart()

        override fun reduce(viewState: ViewState): ViewState {
            return viewState
        }
    }

    sealed class RemoveFoodFromCart : PartialStateChange() {
        object Success : RemoveFoodFromCart()

        override fun reduce(viewState: ViewState): ViewState {
            return viewState
        }
    }
}

data class CartRow(val foodId: Int, val title: String, val count: Int, val eachFoodItemPrice: String, val totalFoodItemsPrice: Int, val image: String?,val isLoading:Boolean=false) {
    fun toDomain() :CartFood{
      return  CartFood(foodId = foodId.toString(), count = count)
    }
}