package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class OrderItemResponse(
        @Json(name = "title")
        val title: String,

        @Json(name = "count")
        val count: Int,

        @Json(name = "foodId")
        val foodId: Int,

        @Json(name = "eachFoodItemPrice")
        val eachFoodItemPrice: Int,

        @Json(name = "totalFoodItemsPrice")
        val totalFoodItemsPrice: Int,

        @Json(name = "discount")
        val discount: Int,

        @Json(name = "packaging")
        val packaging: Int,

        @Json(name = "image")
        val image: String?
)