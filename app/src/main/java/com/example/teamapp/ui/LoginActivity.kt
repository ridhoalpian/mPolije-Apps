package com.example.teamapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.teamapp.PreferencesDataStore
import com.example.teamapp.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : ComponentActivity() {
    private lateinit var binding: LoginActivityBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Pengaturan OnClickListener untuk tombol "Daftar Dulu"
        binding.txtDaftardulu.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val preferenceDataStore = PreferencesDataStore(this)

        // Pengaturan OnClickListener untuk tombol "Masuk"
        binding.btnMasuk.setOnClickListener {
            val email = binding.edtUser.text.toString()
            val password = binding.edtPass.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            if (userId != null) {
                preferenceDataStore.saveValue2(userId)
            }

            // Validasi email
            if (email.isEmpty()) {
                binding.edtUser.error = "Email Harus Diisi"
                binding.edtUser.requestFocus()
                return@setOnClickListener
            }

            // Validasi email yang tidak valid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtUser.error = "Email Tidak Valid"
                binding.edtUser.requestFocus()
                return@setOnClickListener
            }

            // Validasi password
            if (password.isEmpty()) {
                binding.edtPass.error = "Password Harus Diisi"
                binding.edtPass.requestFocus()
                return@setOnClickListener
            }

            // Memanggil fungsi untuk melakukan login Firebase
            LoginFirebase(email, password)
        }
    }

    // Fungsi untuk melakukan login menggunakan Firebase Authentication
    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Selamat datang $email", Toast.LENGTH_SHORT).show()
                    saveLoginStatus()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Email dan Password Salah", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Fungsi untuk menyimpan status login menggunakan SharedPreferences
    private fun saveLoginStatus() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }
}




