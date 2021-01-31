package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.OrderResponse
import com.example.market.domain.entity.Factor
import com.example.market.domain.entity.Order
import com.example.market.domain.entity.OrderItem

class OrderResponseToDomain : Mapper<OrderResponse, Order> {
    override fun invoke(response: OrderResponse): Order {
        return Order(successful = response.successful, factor = response.factor?.let { Factor(discount = response.factor.discount, amount = response.factor.amount,
                grossAmount = response.factor.grossAmount, tax = response.factor.tax, packagingPrice = response.factor.packagingPrice) },
                orderItems = response.orderItems?.map { orderItemResponse ->
                    OrderItem(title = orderItemResponse.title, count = orderItemResponse.count,
                            foodId = orderItemResponse.foodId, eachFoodItemPrice = orderItemResponse.eachFoodItemPrice.toString(), totalFoodItemsPrice = orderItemResponse.totalFoodItemsPrice,
                            discount = orderItemResponse.discount, packaging = orderItemResponse.packaging, image = orderItemResponse.image)
                })
    }

}