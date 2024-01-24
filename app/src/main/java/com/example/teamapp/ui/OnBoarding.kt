package com.example.teamapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.teamapp.R
import com.example.teamapp.databinding.FragmentOnBoardingBinding
import com.example.teamapp.databinding.LoginActivityBinding

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "user_data"
        private const val PREFS_ONBOARDING_SHOWN = "onboarding_shown"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Periksa apakah onboarding sudah pernah ditampilkan
        val onboardingShown = sharedPreferences.getBoolean(PREFS_ONBOARDING_SHOWN, false)

        if (onboardingShown) {
            // Jika onboarding sudah pernah ditampilkan, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Selesai agar tidak kembali ke onboarding
        }

        binding.button.setOnClickListener {
            // Setelah tombol ditekan, tandai bahwa onboarding sudah ditampilkan
            with(sharedPreferences.edit()) {
                putBoolean(PREFS_ONBOARDING_SHOWN, true)
                apply()
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Selesai setelah navigasi ke LoginActivity
        }

        // Panggil fungsi untuk mengatur tampilan onboarding
        setupView()
    }

    // Fungsi untuk mengatur tampilan onboarding, seperti menyembunyikan status bar
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
