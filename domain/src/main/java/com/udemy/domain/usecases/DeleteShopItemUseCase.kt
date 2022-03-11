package com.udemy.domain.usecases

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun deleteShopItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)
    }
}