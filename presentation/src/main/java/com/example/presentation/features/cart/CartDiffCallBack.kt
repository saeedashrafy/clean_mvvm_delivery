package com.example.presentation.features.cart

import androidx.recyclerview.widget.DiffUtil

class CartDiffCallBack : DiffUtil.ItemCallback<CartRow>() {
    override fun areItemsTheSame(oldItem: CartRow, newItem: CartRow): Boolean {
      return   oldItem.foodId == newItem.foodId
    }

    override fun areContentsTheSame(oldItem: CartRow, newItem: CartRow): Boolean {
       return oldItem == newItem
    }
}