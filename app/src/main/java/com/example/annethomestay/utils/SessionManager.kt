package com.example.annethomestay.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
        const val ACCESS_TOKEN = "access_token"
    }

    fun saveUser(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID, 0) // Default value adalah 0
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}

