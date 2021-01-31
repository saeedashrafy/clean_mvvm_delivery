package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class CartItemBody(
        @Json(name = "foodId")
        val foodId: String,
        @Json(name = "count")
        val count: String
)