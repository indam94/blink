package com.example.blink.utils

import android.content.Context
import android.content.SharedPreferences

class CustomSharedPreference(context: Context) {

    private val PREF_FILENAME = "prefs"
    private val PREF_KEY_USER_NICKNAME: String = "myUserNickName"
    val prefs : SharedPreferences = context.getSharedPreferences(PREF_FILENAME, 0)

    var myUserName: String?
        get() = prefs.getString(PREF_KEY_USER_NICKNAME, "X")
        set(value) = prefs.edit().putString(PREF_KEY_USER_NICKNAME, value).apply()

    
    private val PREF_UUID = "uuids"
    private val PREF_KEY_UUID = "key_uuid"
    val prefs_uuid : SharedPreferences = context.getSharedPreferences(PREF_UUID, 0)
    
    var myUuid: String?
        get() = prefs_uuid.getString(PREF_KEY_UUID, "")
        set(value) = prefs_uuid.edit().putString(PREF_KEY_UUID, value).apply()

    private val PREF_IP = "prefs_ip"
    private val PREF_KEY_IP = "myip"
    val pref_ip : SharedPreferences = context.getSharedPreferences(PREF_IP, 0)

    var myIp: String?
        get() = pref_ip.getString(PREF_KEY_IP, "")
        set(value) = pref_ip.edit().putString(PREF_KEY_IP, value).apply()
}