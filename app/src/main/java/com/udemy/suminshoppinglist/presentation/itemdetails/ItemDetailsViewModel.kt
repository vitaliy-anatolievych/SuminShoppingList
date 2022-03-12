package com.udemy.suminshoppinglist.presentation.itemdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udemy.domain.entities.ShopItem
import com.udemy.domain.usecases.AddShopItemUseCase
import com.udemy.domain.usecases.EditShopItemUseCase
import com.udemy.domain.usecases.GetShopItemUseCase
import java.lang.Exception

class ItemDetailsViewModel(
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val getShopItemUseCase: GetShopItemUseCase
): ViewModel() {

    private val _errorInputName = MutableLiveData<String>()
    val errorInputName: LiveData<String>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<String>()
    val errorInputCount: LiveData<String>
        get() = _errorInputCount

    fun addShopItem(name: String, count: Int) {
        val item = ShopItem(name, count, true)
        addShopItemUseCase.addShopItem(item)
    }

    fun editShopItem(name: String, count: Int, itemId: Int) {
        val item = getShopItemUseCase.getShopItem(itemId)
        val newItem = item.copy(name, count, item.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}