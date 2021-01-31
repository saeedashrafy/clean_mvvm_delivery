package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class CartBody(
        @Json(name = "restaurantId")
        val restaurantId: String,

        @Json(name = "device")
        val device: String,

        @Json(name = "basketItems")
        val cartItems: List<CartItemBody>?  ,

        @Json(name = "paymentMethod")
        val paymentMethod :String
)