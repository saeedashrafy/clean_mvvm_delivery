package com.example.market.data.mapper

import com.example.market.core.Mapper
import com.example.market.data.remote.model.CartBody
import com.example.market.data.remote.model.CartItemBody
import com.example.market.domain.entity.CartFood

class CartDomainToBody : Mapper<List<CartFood>, CartBody> {
    override fun invoke(domain: List<CartFood>): CartBody {
        return CartBody(restaurantId = "a3d1e25f-2c3e-4309-ae85-1f73e5bc9f9b",device = "android" ,
                paymentMethod = "bank",
                cartItems = domain.map { cartFood -> CartItemBody(foodId = cartFood.foodId, count = cartFood.count.toString())
                } )

    }
}