package com.example.market.data.local

import android.util.Log
import androidx.room.*
import com.example.market.core.Mapper
import com.example.market.core.dispatchers.CoroutineDispatchers
import com.example.market.data.local.entity.FoodIdAndCountEntity
import com.example.market.domain.entity.CartFood
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.http.DELETE

@Dao
interface MarketDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartFood: FoodIdAndCountEntity)

    @Query("DELETE FROM foods WHERE food_id = :foodId")
    suspend fun deleteFromCart(foodId: Int)

    @Query("SELECT * FROM foods WHERE food_id = :foodId")
    suspend fun getFoodById(foodId: Int): FoodIdAndCountEntity?

    @Query("UPDATE foods SET count =count + 1  WHERE food_id = :foodId")
    suspend fun increaseFoodCount(foodId: Int)

    @Query("UPDATE foods SET count = count -1 WHERE food_id = :foodId")
    suspend fun decreaseFoodCount(foodId: Int)

    @Query("SELECT * from foods")
    suspend fun getAllCartFoods(): List<FoodIdAndCountEntity>

    @Query("SELECT * from foods")
     fun getAllCart(): Flow<List<FoodIdAndCountEntity>?>

    @Query("SELECT * from foods where food_id= :foodId")
    fun getCart(foodId: Int): Flow<FoodIdAndCountEntity?>
}

suspend fun <T, R> safeFetch(ioDisPatcher: CoroutineDispatcher, transform: Mapper<T, R>, query: suspend () -> T?): R? {
    return withContext(ioDisPatcher) {
        Log.d("TAG", "safeFetch")
        query.invoke()?.let { transform(it) }
    }
}

suspend fun <T> safeQuery(ioDisPatcher: CoroutineDispatcher, query: suspend () -> T?) {
    return withContext(ioDisPatcher) {
        query.invoke()
    }
}