package com.example.teamapp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamapp.network.ApiClient
import com.example.teamapp.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    // LiveData untuk mengirim hasil operasi ke antarmuka pengguna
    val resultUser = MutableLiveData<Result>()

    // Metode untuk mengambil data daftar pengguna dari layanan GitHub
    fun getUser() {
        // Menggunakan viewModelScope untuk menjalankan operasi secara asinkron
        viewModelScope.launch {
            flow {
                // Mengambil respons dari layanan GitHub
                val response = ApiClient
                    .githubService
                    .getUserGithub()

                // Mengirim respons sebagai data aliran
                emit(response)
            }.onStart {
                // Memberitahu bahwa proses pengambilan data sedang dimulai
                resultUser.value = Result.Loading(true)
            }.onCompletion {
                // Memberitahu bahwa proses pengambilan data telah selesai
                resultUser.value = Result.Loading(false)
            }.catch {
                // Menangani kesalahan yang mungkin terjadi
                it.printStackTrace()
                resultUser.value = Result.Error(it)
            }.collect {
                // Mengirim hasil pengambilan data ke LiveData
                resultUser.value = Result.Success(it)
            }
        }
    }
}
