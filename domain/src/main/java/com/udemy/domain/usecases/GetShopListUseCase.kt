package com.udemy.domain.usecases

import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    suspend fun getShopList(): List<ShopItem> {
        return shopListRepository.getShopList()
    }
}