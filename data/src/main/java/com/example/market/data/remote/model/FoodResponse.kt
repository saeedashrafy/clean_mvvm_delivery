package com.example.market.data.remote.model

import com.squareup.moshi.Json

internal data class FoodResponse(
        @Json(name = "id")
        val id: Int,
        @Json(name = "title")
        val title: String,
        @Json(name = "img")
        val image: String,
        @Json(name = "ingredient")
        val ingredient: String,
        @Json(name = "price")
        val price: String


)