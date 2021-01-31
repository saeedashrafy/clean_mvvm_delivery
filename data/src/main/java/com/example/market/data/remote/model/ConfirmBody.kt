package com.example.market.data.remote.model

import com.squareup.moshi.Json

internal data class ConfirmBody(
        @Json(name = "grant_type")
        val grantType: String,

        @Json(name = "AppToken")
        val appToken: String,

        @Json(name = "client_id")
        val clientId: String,

        @Json(name = "username")
        val userName: String,

        @Json(name = "password")
        val password: String
)