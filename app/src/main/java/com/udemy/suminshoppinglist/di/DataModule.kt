package com.udemy.suminshoppinglist.di

import com.udemy.data.repositories.ShopListRepositoryImpl
import com.udemy.domain.repositories.ShopListRepository
import org.koin.dsl.module

val dataModule = module {
    single<ShopListRepository> {
        ShopListRepositoryImpl()
    }
}