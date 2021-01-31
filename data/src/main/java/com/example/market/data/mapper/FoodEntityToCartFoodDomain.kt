package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.local.entity.FoodIdAndCountEntity
import com.example.market.domain.entity.CartFood

class FoodEntityToCartFoodDomain : Mapper<FoodIdAndCountEntity, CartFood> {
    override fun invoke(entity: FoodIdAndCountEntity): CartFood {
      return  CartFood(entity.foodId.toString(), entity.count)
    }
}