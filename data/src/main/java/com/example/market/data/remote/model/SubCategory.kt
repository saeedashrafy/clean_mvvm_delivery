package com.example.market.data.remote.model

import com.squareup.moshi.Json

internal data class SubCategory(
        @Json(name = "id")
        val id :Int,
        @Json(name = "title")
        val title:String,
        @Json(name = "food")
        val list :List<FoodResponse>
)