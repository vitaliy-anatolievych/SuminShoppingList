package com.udemy.domain.usecases

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun getShopItem(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}