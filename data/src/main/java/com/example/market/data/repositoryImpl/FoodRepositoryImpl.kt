package com.example.market.data.repositoryImpl

import android.util.Log
import com.example.market.core.Mapper
import com.example.market.core.dispatchers.CoroutineDispatchers
import com.example.market.data.local.MarketDao
import com.example.market.data.local.entity.FoodIdAndCountEntity
import com.example.market.data.remote.Api
import com.example.market.data.remote.model.CartBody
import com.example.market.data.remote.model.CategoryFoodResponse
import com.example.market.data.remote.model.OrderResponse
import com.example.market.data.remote.safeCall
import com.example.market.domain.entity.*
import com.example.market.domain.repository.FoodRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalCoroutinesApi
internal class FoodRepositoryImpl(
        private val apiService: Api,
        private val dao: MarketDao,
        private val entityToDomain: Mapper<FoodIdAndCountEntity, CartFood>,
        private val dispatchers: CoroutineDispatchers,
        private val responseToDomain: Mapper<CategoryFoodResponse, CategoryFood>,
        private val orderResponseToDomain: Mapper<OrderResponse, Order>,
        private val cartFoodDomainToBody: Mapper<List<CartFood>, CartBody>
) : FoodRepository {



    @FlowPreview
    override fun getProduct(): Flow<DomainResult<CategoryFood>> {
        return flow {
            safeCall(dispatcher = dispatchers, responseToDomain) {
                apiService.getCategoryProducts()

            }.let {
                Log.d("TAG","getProduct")
                emit(it) }

        }
    }


    @FlowPreview
    override suspend fun getCartFoodById(cartFood: CartFood): CartFood? {

        return withContext(dispatchers.io) {
            dao.getFoodById(cartFood.foodId.toInt())?.let { entityToDomain(it) }
        }
    }

    override suspend fun deleteFromCart(cartFood: CartFood) {
        withContext(dispatchers.io) {
            dao.deleteFromCart(cartFood.foodId.toInt())


        }
    }

    @FlowPreview
    override suspend fun addCartFood(cartFood: CartFood) {
        withContext(dispatchers.io) {
            dao.addToCart(FoodIdAndCountEntity(cartFood.foodId.toInt(), 1))


        }
    }

    @FlowPreview
    override suspend fun increaseCartFoodCount(cartFood: CartFood) {
        withContext(dispatchers.io) {
            dao.increaseFoodCount(cartFood.foodId.toInt())

        }
    }


    override suspend fun decreaseCartFoodCount(cartFood: CartFood) {
        withContext(dispatchers.io) {
            dao.decreaseFoodCount(cartFood.foodId.toInt())


        }
    }

    override fun getAllCartFood(): Flow<List<CartFood>> {

        return dao.getAllCart().map {
            if (it.isNullOrEmpty()) {
                listOf(CartFood("", 0))
            } else (it.map { entityToDomain(it) })
        }

    }


    override fun calculatePreOrder(list: List<CartFood>): Flow<DomainResult<Order>> {
        return flow {
            safeCall(dispatcher = dispatchers, orderResponseToDomain) {
                apiService.calculate(cartFoodDomainToBody(list))
            }.let {
                Log.d("TAG",it.toString())
                emit(it)
            }

        }
    }




}






