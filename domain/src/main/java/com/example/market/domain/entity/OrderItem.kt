package com.example.market.domain.entity

data class OrderItem(val title: String,
                     val count: Int,
                     val foodId: Int,
                     val eachFoodItemPrice: String,
                     val totalFoodItemsPrice: Int,
                     val discount: Int,
                     val packaging: Int,
                     val image: String?)