package com.example.blink

import android.app.Application
import com.example.blink.utils.CustomSharedPreference


class App: Application() {

    companion object {
        lateinit var prefs: CustomSharedPreference
    }

    override fun onCreate() {

        prefs = CustomSharedPreference(applicationContext)

        super.onCreate()


    }

}