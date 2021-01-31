package com.example.market.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodIdAndCountEntity(
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    val foodId: Int,
    val count: Int
)