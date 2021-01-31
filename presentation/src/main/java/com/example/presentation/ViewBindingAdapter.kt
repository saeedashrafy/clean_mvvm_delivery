package com.example.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding


abstract class ViewBindingAdapter<T, VB : ViewBinding>(
        diffCallback: ViewBindingDiffUtilCallback<T>
) : ListAdapter<T, ViewBindingViewHolder<T, VB>>(diffCallback) {
    override fun onBindViewHolder(holder: ViewBindingViewHolder<T, VB>, position: Int) =
            holder.bind(item = getItem(position))

    override fun onBindViewHolder(
            holder: ViewBindingViewHolder<T, VB>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        holder.bind(item = getItem(position), payloads = payloads)
    }


    protected val ViewGroup.layoutInflater: LayoutInflater
        get() = LayoutInflater.from(this.context)
}