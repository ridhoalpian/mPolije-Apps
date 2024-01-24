package com.example.teamapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.activity.ComponentActivity
import com.example.teamapp.R
import com.example.teamapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : ComponentActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animasi fade in untuk tampilan splash
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.root.startAnimation(fadeIn)

        // Mengakses SharedPreferences untuk memeriksa status login
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        // Handler untuk menunda tindakan selanjutnya selama 3 detik (3000 milidetik)
        Handler().postDelayed({
            // Animasi fade out saat splash selesai
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            binding.root.startAnimation(fadeOut)

            if (isLoggedIn) {
                // Jika pengguna sudah login, arahkan ke MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Jika pengguna belum login, arahkan ke OnBoarding (atau LoginActivity)
                startActivity(Intent(this, OnBoarding::class.java))
            }
            finish() // Selesai untuk menghindari kembali ke SplashScreenActivity
        }, 3000) // Menunda selama 3 detik sebelum menjalankan tindakan selanjutnya
    }
}
