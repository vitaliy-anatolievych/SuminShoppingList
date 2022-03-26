package com.udemy.suminshoppinglist.presentation.main

import android.app.Application
import androidx.lifecycle.*
import com.udemy.domain.entities.ShopItem
import com.udemy.domain.usecases.DeleteShopItemUseCase
import com.udemy.domain.usecases.EditShopItemUseCase
import com.udemy.domain.usecases.GetShopListUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
) : AndroidViewModel(application) {

    private val _shopList = MutableLiveData<List<ShopItem>>()
    val shopList: LiveData<List<ShopItem>>
        get() = _shopList

    init {
        updateShopList()
    }

    fun updateShopList() {
        viewModelScope.launch {
            val list = getShopListUseCase.getShopList()
            _shopList.value = list
        }
    }

    fun deleteItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
            updateShopList()
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
            updateShopList()
        }
    }
}