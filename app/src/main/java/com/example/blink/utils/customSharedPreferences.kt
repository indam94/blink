package com.example.blink.utils

import android.content.Context
import android.content.SharedPreferences

class customSharedPreferences(context: Context) {

    private val PREF_FILENAME = "prefs"
    private val PREF_KEY_USER_NICKNAME: String = "myUserNickName"
    val prefs : SharedPreferences = context.getSharedPreferences(PREF_FILENAME, 0)

    var myUserName: String?
        get() = prefs.getString(PREF_KEY_USER_NICKNAME, "X")
        set(value) = prefs.edit().putString(PREF_KEY_USER_NICKNAME, value).apply()

}