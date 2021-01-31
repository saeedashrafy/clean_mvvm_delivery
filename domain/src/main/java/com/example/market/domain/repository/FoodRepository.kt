package com.example.market.domain.repository

import com.example.market.domain.entity.CartFood
import com.example.market.domain.entity.CategoryFood
import com.example.market.domain.entity.DomainResult
import com.example.market.domain.entity.Order
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    fun getProduct(): Flow<DomainResult<CategoryFood>>

    suspend fun getCartFoodById(food: CartFood): CartFood?

    suspend fun deleteFromCart(cartFood: CartFood)
    suspend fun addCartFood(cartFood: CartFood)

    suspend fun increaseCartFoodCount(cartFood: CartFood)

    suspend fun decreaseCartFoodCount(cartFood: CartFood)

     fun getAllCartFood():  Flow<List<CartFood>>

    fun calculatePreOrder(list: List<CartFood>): Flow<DomainResult<Order>>



}