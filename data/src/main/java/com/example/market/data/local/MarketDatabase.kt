package com.example.market.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.market.data.local.entity.FoodIdAndCountEntity

@Database(entities = [FoodIdAndCountEntity::class], version = 1)
abstract class MarketDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

    companion object {
        const val DB_NAME = "food"

        @Volatile
        private var INSTANCE: MarketDatabase? = null

        fun getInstance(context: Context): MarketDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketDatabase::class.java,
                    DB_NAME
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}