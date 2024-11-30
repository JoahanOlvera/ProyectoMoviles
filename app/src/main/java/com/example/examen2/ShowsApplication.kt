package com.example.examen2

import android.app.Application
import com.example.examen2.data.AppContainer
import com.example.examen2.data.DefaultAppContainer
import com.example.examen2.data.database.AppDatabase

class ShowsApplication: Application() {
    lateinit var container: AppContainer

    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        appDatabase = AppDatabase.getInstance(this)
    }
}