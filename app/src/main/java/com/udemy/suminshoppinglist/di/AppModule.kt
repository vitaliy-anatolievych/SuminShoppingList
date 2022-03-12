package com.udemy.suminshoppinglist.di

import com.udemy.suminshoppinglist.presentation.itemdetails.ItemDetailsViewModel
import com.udemy.suminshoppinglist.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            deleteShopItemUseCase = get(),
            getShopListUseCase = get(),
            editShopItemUseCase = get()
        )
    }

    viewModel<ItemDetailsViewModel> {
        ItemDetailsViewModel(
            editShopItemUseCase = get(),
            addShopItemUseCase = get(),
            getShopItemUseCase = get()
        )
    }
}