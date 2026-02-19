package com.example.shopinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shopinglist.domain.ShopItem

class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areContentsTheSame(
        oldItem: ShopItem,
        newItem: ShopItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: ShopItem,
        newItem: ShopItem
    ): Boolean {
        return oldItem.id == newItem.id
    }
}