package com.udemy.suminshoppinglist.app

import android.app.Application
import com.udemy.suminshoppinglist.di.appModule
import com.udemy.suminshoppinglist.di.dataModule
import com.udemy.suminshoppinglist.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}