package com.example.market.domain.useCase

import com.example.market.domain.entity.*
import com.example.market.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow

class GetPreOrderUseCase(private val foodRepository: FoodRepository) {
    operator fun invoke(list: List<CartFood>): Flow<DomainResult<Order>> = foodRepository.calculatePreOrder(list)

}
