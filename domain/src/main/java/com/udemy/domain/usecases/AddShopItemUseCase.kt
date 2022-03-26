package com.udemy.domain.usecases

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun addShopItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}