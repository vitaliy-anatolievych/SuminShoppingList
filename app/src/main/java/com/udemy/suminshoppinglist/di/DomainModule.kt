package com.udemy.suminshoppinglist.di

import com.udemy.domain.usecases.DeleteShopItemUseCase
import com.udemy.domain.usecases.EditShopItemUseCase
import com.udemy.domain.usecases.GetShopListUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<DeleteShopItemUseCase> {
        DeleteShopItemUseCase(shopListRepository = get())
    }

    factory<GetShopListUseCase> {
        GetShopListUseCase(shopListRepository = get())
    }

    factory<EditShopItemUseCase> {
        EditShopItemUseCase(shopListRepository = get())
    }
}