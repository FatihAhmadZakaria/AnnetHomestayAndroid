package com.example.annethomestay.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_TELEPON = "user_telepon"
        const val USER_NAME = "username"
    }

    fun saveUser(id: Int, email: String, telepon: String) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_TELEPON, telepon)
        editor.apply()
    }


    fun getUserId(): Int {
        return prefs.getInt(USER_ID, 0) // Default value adalah 0
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun getUserTelepon(): String? {
        return prefs.getString(USER_TELEPON, null)
    }

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun getUserProfile(): String? {
        return prefs.getString(USER_NAME, null)
    }
}
