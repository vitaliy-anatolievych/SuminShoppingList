package com.udemy.data.di

import com.udemy.data.providers.ShopListProviders
import dagger.Component

@Component(
    dependencies = [DataDependency::class],
    modules = [DataModule::class]
)
@DataScope
interface DataComponent {

    fun inject(providers: ShopListProviders)

    @Component.Factory
    interface Factory {

        fun create(
            dependency: DataDependency
        ): DataComponent
    }
}
