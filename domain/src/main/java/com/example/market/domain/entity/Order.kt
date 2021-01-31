package com.example.market.domain.entity

data class Order(val successful :Boolean , val factor: Factor?,   val orderItems: List<OrderItem>? )