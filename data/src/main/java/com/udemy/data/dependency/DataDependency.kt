package com.udemy.data.dependency

import android.app.Application

interface DataDependency{
     fun getApplication(): Application
}