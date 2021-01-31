package com.example.market.domain.entity

data class AppSpecification(
    val deviceId: String,
    val appKey: String,
    val appVersion: String,
    val osVersion: String,
    val deviceModel: String
)