package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class ProductResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val tile: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "description")
    val desc: String,
    @Json(name = "imageURL")
    val imageURL: String
)