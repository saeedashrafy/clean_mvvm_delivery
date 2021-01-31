package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.ProductResponse

import com.example.market.domain.entity.Product

class ProductResponseToProductDomain : Mapper<ProductResponse, Product> {
    override fun invoke(response: ProductResponse): Product {
        return Product(
                id = response.id,
                title = response.tile,
                categoryId = response.id,
        )
    }
}