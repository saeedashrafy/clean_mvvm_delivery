package com.example.market.domain.entity

data class Food(val id: Int, val title: String, val count: Int = 0, val image: String, val ingredient: String,
                val price: String)