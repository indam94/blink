package com.example.blink

import android.app.Application

class App: Application() {

    companion object {
        lateinit var prefs: customSharedPreferences
    }

    override fun onCreate() {

        prefs = customSharedPreferences(applicationContext)

        super.onCreate()
    }

}