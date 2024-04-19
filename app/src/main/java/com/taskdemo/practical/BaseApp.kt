package com.taskdemo.practical

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.taskdemo.practical.db.AppDatabase
import java.io.File

class BaseApp : MultiDexApplication() {

    lateinit var dataBase: AppDatabase
    override fun onCreate() {
        super.onCreate()
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        initialize()
    }

    private fun initialize() {
        dataBase = AppDatabase.getDatabase(applicationContext)
    }
}