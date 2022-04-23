package com.udemy.suminshoppinglist.di

import android.app.Application
import com.udemy.data.db.AppDataBase
import com.udemy.data.db.ShopListDao
import com.udemy.data.repositories.ShopListRepositoryImpl
import com.udemy.domain.repositories.ShopListRepository
import com.udemy.suminshoppinglist.di.scope.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDataBase.getInstance(application).shopListDao()
        }
    }
}