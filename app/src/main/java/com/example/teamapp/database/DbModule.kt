package com.example.teamapp.database

import android.content.Context
import androidx.room.Room
import com.example.teamapp.detail.Detaill

class DbModule(private val context: Context) {
    // Membuat instansiasi dari kelas AppDatabase
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "usergithub.db")
        .allowMainThreadQueries() // Mengizinkan akses database di thread utama (hindari ini di produksi)
        .build()

    // Mendapatkan objek UserDao dari AppDatabase untuk berinteraksi dengan database
    val userDao = db.userDao()
}
