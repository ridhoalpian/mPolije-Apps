package com.example.teamapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import androidx.appcompat.app.AppCompatActivity
import coil.transform.CircleCropTransformation
import com.example.teamapp.databinding.FragmentProfileBinding
import com.example.teamapp.model.ResponseDetailUser
import com.example.teamapp.network.ApiClient
import com.example.teamapp.ui.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Profile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Users")
        val preferencesDataStore = PreferencesDataStore(requireContext())
        val dataID = preferencesDataStore.getValue2()

        if (dataID != null) {
            val githubUsernameReference = reference.child(dataID).child("usernameGithub")
            githubUsernameReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val githubUsername = dataSnapshot.getValue(String::class.java)
                    if (githubUsername != null) {
                        getUser(githubUsername)
                    } else {
                        Toast.makeText(activity, "Nama tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(activity, "eror database", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Mendapatkan SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE)

        // Cek apakah pengguna sudah login
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // Mengambil data pengguna dari SharedPreferences
            val username = sharedPreferences.getString("username", "")
            val email = sharedPreferences.getString("email", "")

            val firstChar = username?.take(1)

            // Lakukan sesuatu dengan data pengguna, seperti menampilkannya di UI
        }

        // Mengatur listener untuk tombol logout
        binding.logoutButton.setOnClickListener {
            // Menghapus status login dari SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_logged_in", false)
            editor.apply()

            // Navigasi kembali ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Menutup aktivitas saat logout
        }
        return root
    }

    private fun getUser(usergit: String) {
        val client = ApiClient.githubService.getDetailUserGithub(usergit)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(call: Call<ResponseDetailUser>, response: Response<ResponseDetailUser>) {
                if (response.isSuccessful) {
                    val dataArray = response.body()
                    if (dataArray != null) {
                        binding.apply {
                            profileImage.load(dataArray.avatar_url) {
                                transformations(CircleCropTransformation())
                            }
                            profileRealname.text = dataArray.name
                            profileUsergithub.text = dataArray.login
                            jmlrepos.text = dataArray.public_repos.toString()
                            flwing.text = dataArray.following.toString()
                            flwr.text = dataArray.followers.toString()
                            tmptkerja.text = dataArray.company
                            alamat.text = dataArray.location
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                Toast.makeText(activity, "error client.enqueue", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}
