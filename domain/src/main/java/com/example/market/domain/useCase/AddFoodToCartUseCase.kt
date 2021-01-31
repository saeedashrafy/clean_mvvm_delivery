package com.example.market.domain.useCase

import com.example.market.domain.entity.CartFood
import com.example.market.domain.repository.FoodRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import sun.rmi.runtime.Log


@FlowPreview
class AddFoodToCartUseCase(private val foodRepository: FoodRepository) {
    suspend operator fun invoke(cartFood: CartFood) {
        foodRepository.getCartFoodById(cartFood).let {
            if (it == null) {
                foodRepository.addCartFood(cartFood)
            } else {
                foodRepository.increaseCartFoodCount(cartFood)
            }
        }
    }
}


