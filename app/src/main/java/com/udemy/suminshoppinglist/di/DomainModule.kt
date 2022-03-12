package com.udemy.suminshoppinglist.di

import com.udemy.domain.usecases.*
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

    factory<AddShopItemUseCase> {
        AddShopItemUseCase(
            shopListRepository = get()
        )
    }

    factory<GetShopItemUseCase> {
        GetShopItemUseCase(
            shopListRepository = get()
        )
    }
}