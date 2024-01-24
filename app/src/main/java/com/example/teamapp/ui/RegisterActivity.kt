package com.example.teamapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.teamapp.databinding.RegisterActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : ComponentActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        binding.txtLoginsekarang.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {

            val usernameGithub = binding.edtUsrgithub.text.toString()
            val email = binding.edtEmail.text.toString()
            val username = binding.edtUsername.text.toString()
            val nim = binding.edtNim.text.toString()
            val password = binding.edtPass.text.toString()

            if (usernameGithub.isEmpty() || email.isEmpty() || username.isEmpty() || nim.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            } else {
                // Daftar pengguna ke Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Registrasi Firebase Authentication berhasil
                            val userId = auth.currentUser?.uid ?: ""
                            val dataUser = DataUser(usernameGithub, email, username, nim, password)
                            // Simpan data pengguna ke Firebase Realtime Database
                            database.child("Users").child(userId).setValue(dataUser).addOnSuccessListener {
                                Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Gagal mendaftar dengan Firebase Authentication
                            Toast.makeText(this, "Gagal mendaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
