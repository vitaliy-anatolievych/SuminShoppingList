package com.udemy.suminshoppinglist.app

import android.app.Application
import com.udemy.data.di.DataDependency
import com.udemy.suminshoppinglist.di.DaggerApplicationComponent

class App : Application(), DataDependency.DependencyProvider {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun getDataDependency(): DataDependency = component
}