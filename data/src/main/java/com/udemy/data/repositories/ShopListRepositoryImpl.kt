package com.udemy.data.repositories

import android.app.Application
import com.udemy.data.db.AppDataBase
import com.udemy.data.mappers.ShopListMapper
import com.udemy.domain.entities.ShopItem
import com.udemy.domain.repositories.ShopListRepository

class ShopListRepositoryImpl(
    application: Application
): ShopListRepository {
    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()


    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun getShopList(): List<ShopItem> {
        val list = shopListDao.getShopList()
        return mapper.mapListDbModelToListEntity(list)
    }
}