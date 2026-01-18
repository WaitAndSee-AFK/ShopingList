package com.example.shopinglist.data

import com.example.shopinglist.domain.ShopItem
import com.example.shopinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

        private val shopList = mutableListOf<ShopItem>()
    val _shopList: List<ShopItem>
        get() = shopList.toList()

    private var autoIncrementID  = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementID++
        }
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id: $shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return _shopList
    }
}