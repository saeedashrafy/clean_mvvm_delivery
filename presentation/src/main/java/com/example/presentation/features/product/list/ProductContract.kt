package com.example.presentation.features.product.list

import com.example.market.domain.entity.CartFood
import com.example.market.domain.entity.Failure
import java.io.Serializable


internal sealed class ViewIntent {

    object Retry : ViewIntent()
    object Initial : ViewIntent()
    data class IncreaseCount(val foodRow: FoodRow.Food) : ViewIntent()
    data class DecreaseCount(val foodRow: FoodRow.Food) : ViewIntent()
}


internal data class ViewState(
        val isLoading: Boolean,
        val error: Failure?,
        val foods: List<FoodRow>,
        val cartSum: Int
) {
    companion object {
        fun initialize() =
                ViewState(isLoading = true, error = null, foods = emptyList(), cartSum = 0)
    }
}

internal sealed class SingleEvent {
    data class GetCategoryProductsError(val error: Failure) : SingleEvent()
}

internal sealed class PartialStateChange {
    abstract fun reduce(viewState: ViewState): ViewState

    sealed class GetCategoryProducts : PartialStateChange() {


        override fun reduce(viewState: ViewState): ViewState {
            return when (this) {
                Loading -> viewState.copy(isLoading = true, error = null)
                is FoodData -> viewState.copy(isLoading = false, error = null, foods = foodRows, cartSum = cartCount)
                is FailureData -> viewState.copy(isLoading = false, error = failure)
            }
        }

        data class FoodData(val foodRows: List<FoodRow>, val cartCount: Int) : GetCategoryProducts()

        object Loading : GetCategoryProducts()

        data class FailureData(val failure: Failure) : GetCategoryProducts()

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


enum class RowType {
    Food,
    Category
}

sealed class FoodRow(val rowType: RowType) :Serializable {
    data class Category(val id: Int, val title: String) : FoodRow(RowType.Category)
    data class Food(
            val id: String,
            val title: String,
            val categoryId: Int,
            val count: Int,
            val image: String,
            val ingredient: String,
            val price: String
    ) : FoodRow(RowType.Food),Serializable  {
        fun toDomain(): CartFood = CartFood(id, count)
    }
}

//internal data class CategoryProduct



