package com.udemy.data.di

import android.app.Application
import com.udemy.data.db.AppDataBase
import com.udemy.data.db.ShopListDao
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @DataScope
        @Provides
        fun provideShopListDao(
            application: Application
        ): ShopListDao = AppDataBase.getInstance(application).shopListDao()
    }
}
