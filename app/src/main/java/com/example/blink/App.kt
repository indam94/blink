package com.example.blink

import android.app.Application
import com.example.blink.utils.customSharedPreferences


class App: Application() {

    companion object {
        lateinit var prefs: customSharedPreferences
    }

    override fun onCreate() {

        prefs = customSharedPreferences(applicationContext)

        super.onCreate()


    }

}