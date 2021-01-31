package com.example.market.data.remote.model

import com.squareup.moshi.Json

internal data class RegisterBody(
        @Json(name = "mobile")
        val phoneNumber: String,
        @Json(name = "appToken")
        val appToken: String
)
