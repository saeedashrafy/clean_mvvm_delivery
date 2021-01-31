package com.example.market.domain.useCase

import com.example.market.domain.entity.CartFood
import com.example.market.domain.repository.FoodRepository

class RemoveFoodFromCartUseCase(private val foodRepository: FoodRepository) {
    suspend operator fun invoke(cartFood: CartFood) {
        foodRepository.getCartFoodById(cartFood).let {
            if (it?.count == 1) {
                foodRepository.deleteFromCart(cartFood)
            } else {
                foodRepository.decreaseCartFoodCount(cartFood)
            }
        }
    }
}