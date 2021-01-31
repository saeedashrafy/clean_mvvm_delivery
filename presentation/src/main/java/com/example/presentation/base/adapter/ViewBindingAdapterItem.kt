package com.example.presentation.base.adapter

interface ViewBindingAdapterItem {
    val itemViewType: Int

    companion object {
        const val NORMAL_TYPE = 1
        const val ADD_TYPE = 2
    }
}
