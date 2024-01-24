package com.example.teamapp.network

import com.example.teamapp.model.ResponseDetailUser
import com.example.teamapp.model.ResponseUserGithub
import com.google.firebase.BuildConfig
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubService {
    // Mendapatkan daftar pengguna GitHub
    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserGithub(): MutableList<ResponseUserGithub.Item>

    // Mendapatkan detail pengguna GitHub dengan menggunakan retrofit Call
    @JvmSuppressWildcards
    @GET("users/{username}")
    fun getDetailUserGithub(@Path("username") username: String): Call<ResponseDetailUser>

    // Mendapatkan detail pengguna GitHub secara sinkron dengan menggunakan suspend function
    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUserGithub1(@Path("username") username: String): ResponseDetailUser
}


