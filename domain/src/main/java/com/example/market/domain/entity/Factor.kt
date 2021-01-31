package com.example.market.domain.entity

data class Factor(
        val discount: Int,
        val amount: Int,
        val grossAmount: Int,
        val tax: Int,
        val packagingPrice: Int
)