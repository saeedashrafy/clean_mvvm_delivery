package com.example.market.domain.useCase

import com.example.market.domain.repository.FoodRepository

class GetCategoryProductsUseCase(private val foodRepository: FoodRepository) {
  operator fun invoke() = foodRepository.getProduct()
}