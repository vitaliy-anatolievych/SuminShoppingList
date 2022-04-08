package com.udemy.suminshoppinglist.app

import android.app.Application
import com.udemy.data.dependency.DataDependency
import com.udemy.suminshoppinglist.di.DaggerApplicationComponent

@Suppress("unused")
class App : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}