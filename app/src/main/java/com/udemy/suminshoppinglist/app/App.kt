package com.udemy.suminshoppinglist.app

import android.app.Application
import com.udemy.suminshoppinglist.di.appModule
import com.udemy.suminshoppinglist.di.dataModule
import com.udemy.suminshoppinglist.di.domainModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(listOf(appModule, domainModule, dataModule)) }
    }
}