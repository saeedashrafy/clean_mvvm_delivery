package com.example.market.data.remote.model

import com.squareup.moshi.Json

data class UploadResponse(
    @Json(name="FileId")
    val file: String?,
    @Json(name="FileName")
    val name: String?,
    @Json(name="FileExt")
    val path: String?
)