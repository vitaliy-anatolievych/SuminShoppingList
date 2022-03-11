package com.udemy.domain.usecases

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun editShopItem(shopItem: ShopItem) {
        shopListRepository.editShopItem(shopItem)
    }
}