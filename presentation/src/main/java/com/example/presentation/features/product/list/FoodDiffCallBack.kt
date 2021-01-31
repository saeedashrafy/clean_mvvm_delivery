package com.example.presentation.features.product.list

import androidx.recyclerview.widget.DiffUtil

class FoodDiffCallBack : DiffUtil.ItemCallback<FoodRow>() {

    override fun areItemsTheSame(oldItem: FoodRow, newItem: FoodRow): Boolean {
        return  oldItem.rowType.ordinal == newItem.rowType.ordinal
    }


    override fun areContentsTheSame(oldItem: FoodRow, newItem: FoodRow): Boolean {
        return oldItem == newItem
    }


}