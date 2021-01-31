package com.example.market.domain.entity

data class AppInit(private val inReview: Boolean,
                   private val validLocation: Boolean,
                   private val ip: String?)