package com.udemy.data.mappers

import com.udemy.data.models.ShopItemDbModel
import com.udemy.domain.entities.ShopItem

class ShopListMapper {

    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enabled = shopItem.enabled
        )
    }

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            enabled = shopItemDbModel.enabled,
            id = shopItemDbModel.id
        )
    }

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>): List<ShopItem> {
        return list.map {
            mapDbModelToEntity(it)
        }
    }
}