package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class AppSpecificationBody(
    @Json(name = "deviceId")
    val deviceId: String,

    @Json(name = "appKey")
    val appKey: String,

    @Json(name = "appVersion")
    val appVersion: String,

    @Json(name = "osVersion")
    val osVersion: String,

    @Json(name = "deviceModel")
    val deviceModel :String
)