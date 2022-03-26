package com.udemy.suminshoppinglist.di

import com.udemy.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    factory {
        DeleteShopItemUseCase(shopListRepository = get())
    }

    factory {
        GetShopListUseCase(shopListRepository = get())
    }

    factory {
        EditShopItemUseCase(shopListRepository = get())
    }

    factory {
        AddShopItemUseCase(
            shopListRepository = get()
        )
    }

    factory {
        GetShopItemUseCase(
            shopListRepository = get()
        )
    }
}