package com.udemy.data.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.udemy.data.db.ShopListDao
import com.udemy.data.di.DaggerDataComponent
import com.udemy.data.di.DataDependency
import com.udemy.data.mappers.ShopListMapper
import com.udemy.domain.entities.ShopItem
import javax.inject.Inject

class ShopListProviders : ContentProvider() {

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val component by lazy {
        DaggerDataComponent.factory().create(
            (context as DataDependency.DependencyProvider).getDataDependency()
        )
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.udemy.suminshoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
        addURI("com.udemy.suminshoppinglist", "shop_items/#", GET_SHOP_ITEMS_BY_ID_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        // Если создание провайдера прошло успешно, то вернуть true
        return true
    }

    // В этом методе можно запросить данные
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor() // получим список всех записей из базы данных
            }
            else -> null // Если пришёл некорректный запрос
        }
    }


    override fun getType(uri: Uri): String? {
        TODO()
    }

    // Вставлять данные
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                if (contentValues == null) return null
                val id = contentValues.getAsInteger("id")
                val name = contentValues.getAsString("name")
                val count = contentValues.getAsInteger("count")
                val enabled = contentValues.getAsBoolean("enabled")

                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )

                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    // Удалять данные
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return shopListDao.deleteShopItemSync(id)
            }
        }
        return 0
    }

    // Обновлять данные
    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        p2: String?,
        p3: Array<out String>?,
    ): Int {
        TODO()
    }

    companion object {
        private const val GET_SHOP_ITEMS_QUERY = 100
        private const val GET_SHOP_ITEMS_BY_ID_QUERY = 101
    }
}