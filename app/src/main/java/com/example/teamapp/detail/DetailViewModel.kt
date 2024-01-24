package com.example.teamapp.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.teamapp.database.DbModule
import com.example.teamapp.model.ResponseUserGithub
import com.example.teamapp.network.ApiClient
import com.example.teamapp.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: DbModule) : ViewModel() {
    val resultDetaiUser = MutableLiveData<Result>()
    val resultSuksesFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false

    // Metode untuk menambahkan atau menghapus pengguna dari daftar favorit
    fun setFavorite(item: ResponseUserGithub.Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    // Menghapus pengguna dari daftar favorit dalam database lokal
                    db.userDao.delete(item)
                    resultDeleteFavorite.value = true
                } else {
                    // Menambahkan pengguna ke dalam daftar favorit dalam database lokal
                    db.userDao.insert(item)
                    resultSuksesFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    // Metode untuk mencari apakah pengguna sudah ada di daftar favorit
    fun findFavorite(id: Int, listenFavorite: (Any?) -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                isFavorite = true
            }
        }
    }

    // Metode untuk mengambil detail pengguna GitHub dari API
    fun getDetailUser(username : String) {
        // Menggunakan viewModelScope untuk menjalankan operasi asinkron
        viewModelScope.launch {
            flow {
                val response = ApiClient
                    .githubService
                    .getDetailUserGithub1(username)

                emit(response)
            }.onStart {
                // Memberitahu bahwa proses pengambilan data sedang dimulai
                resultDetaiUser.value = Result.Loading(true)
            }.onCompletion {
                // Memberitahu bahwa proses pengambilan data telah selesai
                resultDetaiUser.value = Result.Loading(false)
            }.catch {
                // Menangani kesalahan yang terjadi selama pengambilan data
                it.printStackTrace()
                resultDetaiUser.value = Result.Error(it)
            }.collect {
                // Mengirimkan hasil pengambilan data sukses
                resultDetaiUser.value = Result.Success(it)
            }
        }
    }

    // Factory class untuk ViewModel
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}
