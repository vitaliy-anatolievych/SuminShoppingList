package com.udemy.data.repositories

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

class ShopListRepositoryImpl: ShopListRepository {
    private val list = mutableListOf<ShopItem>()
    private var autoIncrementId = 0


    init {
        for (i in 0..20) {
            val shopItem = ShopItem("Элемент $i", i,Random.nextBoolean())
            addShopItem(shopItem)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        shopItem.id = autoIncrementId++
        list.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        list.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val _list = list.toList()

        _list.forEachIndexed { index, currentItem ->
            if (currentItem.id == shopItem.id) {
                list.removeAt(index)
                list.add(index, shopItem)
                return@forEachIndexed
            }
        }
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return list.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return list
    }
}