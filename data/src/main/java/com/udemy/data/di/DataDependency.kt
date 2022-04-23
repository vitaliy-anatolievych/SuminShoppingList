package com.udemy.data.di

import android.app.Application

interface DataDependency {

    interface DependencyProvider {

        fun getDataDependency(): DataDependency
    }

    fun getApplication(): Application
}
