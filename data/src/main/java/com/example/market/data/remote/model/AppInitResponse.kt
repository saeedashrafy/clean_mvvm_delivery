package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class AppInitResponse(
    @Json(name = "inReview")
     val inReview: Boolean,

    @Json(name = "validLocation")
     val validLocation: Boolean,

    @Json(name = "ip")
     val ip: String?
)
