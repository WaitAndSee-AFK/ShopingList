package com.example.shopinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopinglist.domain.ShopItem
import com.example.shopinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    val _shopList: List<ShopItem>
        get() = shopList.toList()

    private var autoIncrementID  = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementID++
        }
        shopList.add(shopItem)
        updateLD()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLD()
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

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateLD() {
        shopListLD.value = _shopList
    }

    init {
        for (i in 0 until 10) {
            addShopItem(ShopItem("Name + $i", i, true))
        }
    }

}