package com.example.market.domain.entity

data class Authenticate(val hasPassword: Boolean,
                        val containsKey: Boolean,
                        val fullName: String?)