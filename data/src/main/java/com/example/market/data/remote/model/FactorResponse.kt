package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class FactorResponse(
        @Json(name = "discount")
        val discount: Int,

        @Json(name = "amount")
        val amount: Int,

        @Json(name = "grossAmount")
        val grossAmount: Int,

        @Json(name = "tax")
        val tax: Int,

        @Json(name = "packagingPrice")
        val packagingPrice: Int
)