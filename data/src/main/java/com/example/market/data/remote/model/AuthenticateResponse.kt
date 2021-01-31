package com.example.market.data.remote.model

import com.example.market.domain.entity.Authenticate
import com.squareup.moshi.Json

internal data class AuthenticateResponse(
        @Json(name = "hasPassword")
        val hasPassword: Boolean,
        @Json(name ="block")
        val block:Boolean,
        @Json(name = "containsKey")
        val containsKey :Boolean,
        @Json(name="fullname")
        val fullName :String?
)
{
}
