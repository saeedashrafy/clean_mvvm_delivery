package com.example.presentation.features.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.leftJoinFirst
import com.example.market.domain.entity.CartFood
import com.example.market.domain.entity.Food
import com.example.market.domain.entity.map
import com.example.market.domain.useCase.AddFoodToCartUseCase
import com.example.market.domain.useCase.GetAllCartUseCase
import com.example.market.domain.useCase.GetCategoryProductsUseCase
import com.example.market.domain.useCase.RemoveFoodFromCartUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

@Suppress("USELESS_CAST")
@FlowPreview
@ExperimentalCoroutinesApi
internal class ProductViewModel(
        private val getCategoryProductsUseCase: GetCategoryProductsUseCase,
        private val getAllCartUseCase: GetAllCartUseCase,
        private val addFoodToCartUseCase: AddFoodToCartUseCase,
        private val removeFoodFromCartUseCase: RemoveFoodFromCartUseCase
) :
        ViewModel() {
    private val _intentFlow = MutableSharedFlow<ViewIntent>(
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _eventFlow = MutableSharedFlow<SingleEvent>(
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val viewState: StateFlow<ViewState>
    val singleEvent: Flow<SingleEvent> = _eventFlow


    suspend fun processIntent(viewIntent: ViewIntent) = _intentFlow.emit(viewIntent)

    init {
        val initialVS = ViewState.initialize()
        viewState = merge(
                _intentFlow.filterIsInstance<ViewIntent.Initial>().take(1),
                _intentFlow.filterNot { it is ViewIntent.Initial }
        )
                //.shareIn(viewModelScope, SharingStarted.WhileSubscribed())
                .toPartialChangeFlow()
                .sendSingleEvent()
                .scan(initialVS) { state, change -> change.reduce(state) }
                .stateIn(viewModelScope, SharingStarted.Eagerly, initialVS)


    }

    private fun Flow<ViewIntent>.toPartialChangeFlow(): Flow<PartialStateChange> {

        val getProductChanges = getCategoryProductsUseCase()
                .flatMapConcat {
                    domainResult ->
                    getAllCartUseCase().map {
                        cartFoods ->
                        domainResult.map { categoryProduct ->
                            val categoryList = categoryProduct.categoryList.map { category ->
                                val list =
                                        category.foods.leftJoinFirst(cartFoods.filterNot { cartFood -> cartFood.foodId == "" }) { food: Food, cartFood: CartFood ->
                                            food.id == cartFood.foodId.toInt()
                                        }.map { pair: Pair<Food, CartFood?> ->
                                            if (pair.second == null) pair.first
                                            else pair.first.copy(count = pair.second!!.count)
                                        }
                                category.copy(foods = list as List<Food>)
                            }
                            categoryProduct.copy(
                                    categoryList = categoryList,
                                    cartSum = cartFoods.sumBy { foodIdAndCountEntity -> foodIdAndCountEntity.count })
                        }
                    }
                }
                .map {
                    it.fold({ failure ->
                        PartialStateChange.GetCategoryProducts.FailureData(failure) as PartialStateChange.GetCategoryProducts
                    }, { categoryProduct ->
                        val fooRowList = categoryProduct.categoryList.flatMap { category ->
                            listOf(
                                    FoodRow.Category(category.id, category.title),
                                    *category.foods.map { product ->
                                        FoodRow.Food(
                                                product.id.toString(),
                                                product.title,
                                                category.id,
                                                product.count,
                                                product.image,
                                                product.ingredient,
                                                product.price
                                        )
                                    }.toTypedArray()
                            )
                        }
                        PartialStateChange.GetCategoryProducts.FoodData(fooRowList, categoryProduct.cartSum) as PartialStateChange.GetCategoryProducts
                    })
                            .let {
                                return@map it as PartialStateChange
                            }
                }
                .onStart { emit(PartialStateChange.GetCategoryProducts.Loading) }


        val addFoodToCartChanges = filterIsInstance<ViewIntent.IncreaseCount>()
                .map {
                    it.foodRow.toDomain()
                }
                .flatMapConcat { cartFood ->
                    flow { addFoodToCartUseCase.invoke(cartFood).let { emit(it) } }
                }
                .map { PartialStateChange.AddFoodToCart.Success as PartialStateChange.AddFoodToCart }

        val removeFoodCartChanges = filterIsInstance<ViewIntent.DecreaseCount>()
                .map { it.foodRow.toDomain() }
                .flatMapConcat { cartFood ->
                    flow { removeFoodFromCartUseCase.invoke(cartFood).let { emit(it) } }
                }
                .map { PartialStateChange.RemoveFoodFromCart.Success as PartialStateChange.RemoveFoodFromCart }

        return merge(
                filterIsInstance<ViewIntent.Initial>()
                        .flatMapConcat { getProductChanges },
                addFoodToCartChanges,
                removeFoodCartChanges,
                filterIsInstance<ViewIntent.Retry>()
                        .flatMapMerge { getProductChanges }
        )

    }

    private fun Flow<PartialStateChange>.sendSingleEvent(): Flow<PartialStateChange> {
        return onEach {
            val event = when (it) {
                is PartialStateChange.GetCategoryProducts.FailureData -> SingleEvent.GetCategoryProductsError(
                        error = it.failure
                )
                is PartialStateChange.GetCategoryProducts.FoodData -> return@onEach
                PartialStateChange.GetCategoryProducts.Loading -> return@onEach
                PartialStateChange.AddFoodToCart.Success -> return@onEach
                PartialStateChange.RemoveFoodFromCart.Success -> return@onEach
            }
            _eventFlow.emit(event)
        }
    }



}