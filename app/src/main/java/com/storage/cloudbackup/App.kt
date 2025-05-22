package com.storage.cloudbackup

import android.app.Application
import android.util.Log

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            handleUncaughtException(e)
        }
    }

    private fun handleUncaughtException(e: Throwable) {
        Log.d("TAG1", e.message.toString())
    }
}