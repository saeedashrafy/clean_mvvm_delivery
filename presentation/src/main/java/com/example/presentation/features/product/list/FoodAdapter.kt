package com.example.presentation.features.product.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.presentation.R
import com.example.presentation.databinding.RowHeaderBinding
import com.example.presentation.databinding.RowProductBinding
import com.example.presentation.utils.AppGlide

import com.example.presentation.utils.convertToArabic


class FoodAdapter : ListAdapter<FoodRow, RecyclerView.ViewHolder>(FoodDiffCallBack()) {

    private lateinit var listener: FoodCellClickListener

    fun setCellClickListener(foodCellClickListener: FoodCellClickListener) {
        this.listener = foodCellClickListener
    }

    class HeaderViewHolder(val binding: RowHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(headerRow: FoodRow.Category) {
            binding.textViewHeader.text = headerRow.title
        }

    }

    class ProductViewHolder(val binding: RowProductBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(foodRow: FoodRow.Food) {
            binding.textViewProductTitle.text = foodRow.title
            binding.textViewProductCount.text = foodRow.count.toString().convertToArabic()
            binding.textViewProductIngredient.text = foodRow.ingredient
            // binding.textViewProductPrice.text =foodRow.price
            val radius = binding.root.context.resources.getDimensionPixelSize(R.dimen.corner_radius_food)
            val imagePath = foodRow.image.replace("#SIZEOFIMAGE#", "280x175")
            AppGlide.with(this.itemView.context)
                    .load(imagePath)
                    .placeholder(R.drawable.place_holder)
                    .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radius)))
                    .into(binding.imageViewFood)
            "${(foodRow.price).convertToArabic()} تومان".also { binding.textViewProductPrice.text = it }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (RowType.values()[viewType]) {
                RowType.Category -> HeaderViewHolder(
                        RowHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
                RowType.Food -> ProductViewHolder(
                        RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val foodRow = getItem(position)) {
            is FoodRow.Food -> {
                val holder = (holder as ProductViewHolder)
                holder.bind(foodRow)


                holder.binding.textViewProductCount.visibility =
                        if (foodRow.count != 0) View.VISIBLE else View.GONE
                holder.binding.imageButtonRemove.visibility =
                        if (foodRow.count != 0) View.VISIBLE else View.GONE
                holder.binding.imageButtonAdd.visibility =
                        if (foodRow.count != 0) View.VISIBLE else View.GONE
                holder.binding.buttonAdd.visibility =
                        if (foodRow.count == 0) View.VISIBLE else View.INVISIBLE
                holder.itemView.setOnClickListener {
                    listener.onClickRow(foodRow)
                }
                holder.binding.buttonAdd.setOnClickListener { listener.clickedButtonAdd(foodRow) }
                holder.binding.imageButtonAdd.setOnClickListener { listener.clickedButtonAdd(foodRow) }
                holder.binding.imageButtonRemove.setOnClickListener { listener.clickedButtonRemove(foodRow) }
            }
            is FoodRow.Category -> (holder as HeaderViewHolder).bind(foodRow)
        }

    }


    override fun getItemViewType(position: Int): Int = getItem(position).rowType.ordinal
}

interface FoodCellClickListener {
    fun clickedButtonAdd(foodRow: FoodRow.Food)
    fun clickedButtonRemove(foodRow: FoodRow.Food)
    fun onClickRow(foodRow: FoodRow.Food)
}