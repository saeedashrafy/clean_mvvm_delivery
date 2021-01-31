package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.CategoryFoodResponse

import com.example.market.domain.entity.Category
import com.example.market.domain.entity.CategoryFood
import com.example.market.domain.entity.Food

internal class CategoryProductResponseToDomain : Mapper<CategoryFoodResponse, CategoryFood> {
    override fun invoke(response: CategoryFoodResponse): CategoryFood {
        return CategoryFood(categoryList = response.list.map { category ->
            Category(
                    id = category.id,
                    title = category.title,
                    foods = category.list.flatMap { subCategory ->
                        subCategory.list.map { food ->
                            Food(id = food.id, title = food.title, image = food.image, ingredient = food.ingredient, price = food.price)
                        }
                    })
        } )
    }
}