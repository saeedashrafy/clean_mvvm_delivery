package com.example.presentation.features.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.presentation.R
import com.example.presentation.databinding.RowCartBinding
import com.example.presentation.utils.AppGlide

import com.example.presentation.utils.convertToArabic

class CartAdapter : ListAdapter<CartRow, CartAdapter.CartViewHolder>(CartDiffCallBack()) {

    private lateinit var listener: CartCellClickListener
    fun setCartCellClickListener(listener: CartCellClickListener) {
        this.listener = listener
    }

    class CartViewHolder(val binding: RowCartBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartRow: CartRow) {
            binding.textViewCartTitle.text = cartRow.title
            binding.textViewCartCount.text = cartRow.count.toString().convertToArabic()
            val radius = binding.root.context.resources.getDimensionPixelSize(R.dimen.corner_radius_food)
            val imagePath = cartRow.image?.replace("#SIZEOFIMAGE#", "280x175")
            AppGlide.with(this.itemView.context)
                    .load(imagePath)
                    .placeholder(R.drawable.place_holder)
                    .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radius)))
                    .into(binding.imageViewFood)
            "${(cartRow.eachFoodItemPrice).convertToArabic()} تومان".also { binding.textViewCartPrice.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(RowCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val orderItem = getItem(position)
        holder.bind(orderItem)
        holder.binding.progressCart.isVisible = orderItem.isLoading
        holder.binding.textViewCartCount.isVisible = !orderItem.isLoading
        holder.binding.imageButtonAdd.isEnabled = !orderItem.isLoading
        holder.binding.imageButtonRemove.isEnabled = !orderItem.isLoading
        holder.binding.imageButtonAdd.setOnClickListener {
            holder.binding.progressCart.isVisible = true
            holder.binding.textViewCartCount.text = ""
            holder.binding.imageButtonAdd.isEnabled = false
            holder.binding.imageButtonRemove.isEnabled = false
            listener.clickedButtonAdd(orderItem) }
        holder.binding.imageButtonRemove.setOnClickListener {
            holder.binding.progressCart.isVisible = true
            holder.binding.textViewCartCount.text = ""
            holder.binding.imageButtonAdd.isEnabled = false
            holder.binding.imageButtonRemove.isEnabled = false
            listener.clickedButtonRemove(orderItem) }
    }


    interface CartCellClickListener {
        fun clickedButtonAdd(cartRow: CartRow)
        fun clickedButtonRemove(cartRow: CartRow)
    }
}