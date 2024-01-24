package com.example.teamapp.utils

sealed class Result {
    // Subkelas data yang merepresentasikan hasil yang berhasil dengan data T
    data class Success<out T>(val data: T) : Result()

    // Subkelas data yang merepresentasikan hasil yang berisi kesalahan (exception)
    data class Error(val exception: Throwable) : Result()

    // Subkelas data yang merepresentasikan hasil sedang dalam proses (misalnya, loading)
    data class Loading(val isLoading: Boolean) : Result()
}
