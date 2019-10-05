package com.example.blink

import android.app.Application
import android.content.Intent
import android.support.v4.content.FileProvider
import java.io.File
import android.support.v4.content.ContextCompat.startActivity
import android.R



class App: Application() {

    companion object {
        lateinit var prefs: customSharedPreferences
    }

    override fun onCreate() {

        prefs = customSharedPreferences(applicationContext)

        super.onCreate()


    }

}