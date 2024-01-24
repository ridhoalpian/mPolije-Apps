package com.example.teamapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.teamapp.model.ResponseUserGithub

@Dao
interface UserDao {
    // Fungsi untuk menyisipkan (insert) data pengguna ke dalam tabel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUserGithub.Item)

    // Fungsi untuk mengambil semua data pengguna dari tabel sebagai LiveData
    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<ResponseUserGithub.Item>>

    // Fungsi untuk mencari data pengguna berdasarkan ID
    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): ResponseUserGithub.Item

    // Fungsi untuk menghapus data pengguna dari tabel
    @Delete
    fun delete(user: ResponseUserGithub.Item)
}
