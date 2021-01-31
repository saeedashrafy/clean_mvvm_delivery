package com.example.market.data.remote.model

import com.squareup.moshi.Json

internal data class CategoryFoodResponse(
  @Json(name ="categories" )
  val list: List<Category>
)