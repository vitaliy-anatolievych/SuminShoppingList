package com.udemy.suminshoppinglist.di

import android.app.Application
import com.udemy.suminshoppinglist.providers.ShopListProviders
import com.udemy.suminshoppinglist.di.scope.ApplicationScope
import com.udemy.suminshoppinglist.presentation.itemdetails.ShopItemFragment
import com.udemy.suminshoppinglist.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(shopItemFragment: ShopItemFragment)

    fun inject(shopListProviders: ShopListProviders)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}