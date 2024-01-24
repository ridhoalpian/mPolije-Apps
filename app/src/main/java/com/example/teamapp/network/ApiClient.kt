package com.example.teamapp.network

import com.google.firebase.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {
    // Inisialisasi OkHttpClient dengan interceptor untuk logging
    private val okhttp = OkHttpClient.Builder()
        .apply {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    // Inisialisasi Retrofit dengan baseUrl GitHub API, OkHttpClient, dan konverter Gson
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/") // Base URL untuk API GitHub
        .client(okhttp) // Menggunakan OkHttpClient yang telah dikonfigurasi
        .addConverterFactory(GsonConverterFactory.create()) // Menggunakan Gson untuk mengonversi JSON ke objek Kotlin
        .build()

    // Membuat layanan Retrofit untuk GitHub
    val githubService = retrofit.create<GithubService>()
}
