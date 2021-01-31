package com.example.presentation.features.product.details

import com.example.market.domain.entity.CartFood
import com.example.market.domain.entity.Failure
import com.example.presentation.features.product.list.FoodRow
import com.example.presentation.features.product.list.PartialStateChange


internal sealed class ViewIntent() {
    data class Increment(val food: FoodRow.Food) : ViewIntent()
    data class Decrement(val food: FoodRow.Food) : ViewIntent()
    data class Init(val food:FoodRow.Food?) :ViewIntent()
}

internal data class ViewState(val cartFood: CartFood?) {
    companion object {
        fun initialize(): ViewState = ViewState(cartFood = null)
    }
}

internal sealed class PartialStateChanges {
    abstract fun reduce(viewState: ViewState): ViewState

    sealed class AddFoodToCart : PartialStateChanges() {

        object Success : AddFoodToCart()

        override fun reduce(viewState: ViewState): ViewState {
            return viewState.copy()
        }

    }

    sealed class RemoveFoodFromCart : PartialStateChanges() {
        object Success : RemoveFoodFromCart()

        override fun reduce(viewState: ViewState): ViewState {
            return viewState.copy()
        }
    }

    sealed class  GetFood : PartialStateChanges(){


        data class Success(val cartFood: CartFood) :GetFood() {
            override fun reduce(viewState: ViewState): ViewState {
                return  viewState.copy(cartFood =cartFood )
            }
        }

    }
}