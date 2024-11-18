package com.example.annethomestay.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_ID = "user_id"
        const val ACCESS_TOKEN = "access_token"
    }

    // Menyimpan id_reservasi
    fun saveReservasiId(id: Int) {
        val editor = prefs.edit()
        editor.putInt("id_reservasi", id)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getReservasiId(): Int {
        return prefs.getInt("id_reservasi", -1) // -1 jika tidak ada id yang disimpan
    }

    // Menyimpan id_reservasi
    fun saveIdKendaraan(id: Int) {
        val editor = prefs.edit()
        editor.putInt("id_kendaraan", id)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getIdKendaraan(): Int {
        return prefs.getInt("id_kendaraan", -1) // -1 jika tidak ada id yang disimpan
    }

    // Menyimpan id_reservasi
    fun saveIdProperti(id: Int) {
        val editor = prefs.edit()
        editor.putInt("id_properti", id)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getIdProperti(): Int {
        return prefs.getInt("id_properti", -1) // -1 jika tidak ada id yang disimpan
    }

    // Menyimpan id_reservasi
    fun saveTglMulai(tgl_mulai: String) {
        val editor = prefs.edit()
        editor.putString("tgl_mulai", tgl_mulai)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getTglMulai(): String {
        return prefs.getString("tgl_mulai","") ?:"" // -1 jika tidak ada id yang disimpan
    }

    // Menyimpan id_reservasi
    fun saveTglSelesai(tgl_selesai: String) {
        val editor = prefs.edit()
        editor.putString("tgl_selesai", tgl_selesai)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getTglSelesai(): String {
        return prefs.getString("tgl_selesai","") ?:"" // -1 jika tidak ada id yang disimpan
    }

    fun saveUser(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }
    // Menyimpan id_reservasi
    fun saveNama(nama: String) {
        val editor = prefs.edit()
        editor.putString("nama", nama)
        editor.apply()
    }

    // Menyimpan id_reservasi
    fun saveEmail(email: String) {
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }

    // Mendapatkan id_reservasi yang disimpan
    fun getEmail(): String {
        return prefs.getString("email","") ?:"" // -1 jika tidak ada id yang disimpan
    }


    // Mendapatkan id_reservasi yang disimpan
    fun getNama(): String {
        return prefs.getString("nama","") ?:"" // -1 jika tidak ada id yang disimpan
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

