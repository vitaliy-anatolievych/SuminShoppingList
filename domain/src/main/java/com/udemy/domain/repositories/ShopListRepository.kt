package com.udemy.domain.repositories

import com.udemy.domain.entities.ShopItem

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): List<ShopItem>
}