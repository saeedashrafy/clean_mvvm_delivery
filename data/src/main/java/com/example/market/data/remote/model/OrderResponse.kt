package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class OrderResponse(
        @Json(name = "successful")
        val successful: Boolean,

        @Json(name = "factor")
        val factor: FactorResponse?,

        @Json(name = "foodItems")
        val orderItems: List<OrderItemResponse>?


)