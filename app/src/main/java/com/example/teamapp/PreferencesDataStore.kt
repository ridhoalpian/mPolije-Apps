package com.example.teamapp

import android.content.Context
import android.content.SharedPreferences

class PreferencesDataStore(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    // Fungsi untuk menyimpan nilai email pengguna
    fun saveValue1(value: String) {
        sharedPreferences.edit().putString("USER_EMAIL", value).apply()
    }

    // Fungsi untuk mendapatkan nilai email pengguna
    fun getValue1(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }

    // Fungsi untuk menyimpan nilai UID pengguna
    fun saveValue2(value: String) {
        sharedPreferences.edit().putString("USER_UID", value).apply()
    }

    // Fungsi untuk mendapatkan nilai UID pengguna
    fun getValue2(): String? {
        return sharedPreferences.getString("USER_UID", null)
    }

    // Fungsi untuk menghapus kedua nilai yang tersimpan
    fun eraseValues() {
        sharedPreferences.edit().remove("USER_EMAIL").remove("USER_UID").apply()
    }
}
