package com.example.blink

import android.app.Application
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.content.ContextCompat.startActivity
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