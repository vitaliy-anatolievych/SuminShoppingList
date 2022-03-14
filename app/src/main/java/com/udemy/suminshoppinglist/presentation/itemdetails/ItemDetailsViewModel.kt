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
    private val getShopItemUseCase: GetShopItemUseCase,
) : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    /** Если в Activity никак не будет использоваться объект,
    * можно установить как Unit */
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(itemId: Int): ShopItem {
        return getShopItemUseCase.getShopItem(itemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateInput(name, count)) {
            val item = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(item)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?, itemId: Int) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateInput(name, count)) {
            val item = getShopItemUseCase.getShopItem(itemId)
            val newItem = item.copy(name, count)
            editShopItemUseCase.editShopItem(newItem)
            finishWork()
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true

        if (name.isBlank() or name.isEmpty()) {
            _errorInputName.value = true
            result = false
        }

        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }

        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun resetErrorInputName() {
        _errorInputName.value = null
    }

    private fun resetErrorInputCount() {
        _errorInputCount.value = null
    }
}