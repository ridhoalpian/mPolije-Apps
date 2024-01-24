package com.example.teamapp.favorit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teamapp.database.DbModule

// ViewModel ini digunakan untuk mengelola data terkait favorit pengguna.
class FavoriteViewModel(private val dbModule: DbModule) : ViewModel() {

    // Fungsi ini mengambil daftar pengguna favorit dari modul basis data.
    fun getUserFavorite() = dbModule.userDao.loadAll()

    // Factory untuk membuat instance FavoriteViewModel dengan dependensi DbModule.
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}
