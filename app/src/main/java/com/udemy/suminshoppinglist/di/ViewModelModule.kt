package com.udemy.suminshoppinglist.di

import androidx.lifecycle.ViewModel
import com.udemy.suminshoppinglist.di.annotations.ViewModelKey
import com.udemy.suminshoppinglist.presentation.itemdetails.ItemDetailsViewModel
import com.udemy.suminshoppinglist.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ItemDetailsViewModel::class)
    fun bindItemDetailsViewModel(mainViewModel: ItemDetailsViewModel): ViewModel
}