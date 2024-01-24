package com.example.teamapp.detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.CircleCropTransformation
import com.example.teamapp.R
import com.example.teamapp.database.DbModule
import com.example.teamapp.databinding.FragmentHomeDetailBinding
import com.example.teamapp.model.ResponseDetailUser
import com.example.teamapp.model.ResponseUserGithub
import com.example.teamapp.utils.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Detaill : Fragment() {
    private lateinit var binding: FragmentHomeDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailViewModel.Factory(DbModule(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflasi tata letak fragment menggunakan data binding
        binding = FragmentHomeDetailBinding.inflate(inflater, container, false)

// Mengambil objek Parcelable dengan kunci "item" dari argument Fragment
        val item = requireArguments().getParcelable<ResponseUserGithub.Item>("item")

// Mengambil nama pengguna (username) dari objek Parcelable jika tersedia, atau mengaturnya menjadi string kosong jika tidak tersedia.
        val username = item?.login ?: ""


        // Mengamati hasil pengambilan detail pengguna dari ViewModel
        viewModel.resultDetaiUser.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetailUser
                    // Memuat gambar pengguna dengan transformasi lingkaran
                    binding.image.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.nama.text = user.name
                    binding.location.text = user.location
                    binding.company.text = user.company
                    binding.follow.text = user.following.toString()
                    binding.follower.text = user.followers.toString()
                    binding.repo.text = user.public_repos.toString()
                    binding.username.text = user.login
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {
                    // Menampilkan atau menyembunyikan progressBar
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        // Mengambil detail pengguna dari ViewModel
        viewModel.getDetailUser(username)

        // Mengamati hasil apakah pengguna sudah ada di daftar favorit
        viewModel.resultSuksesFavorite.observe(viewLifecycleOwner) { result ->
            val isFavorite = result as? Boolean
            if (isFavorite == true) {
                // Mengubah warna ikon tombol favorit menjadi merah
                binding.btnFavorite.changeIconColor(R.color.red)
            }
        }

        // Mengamati hasil apakah pengguna telah dihapus dari daftar favorit
        viewModel.resultDeleteFavorite.observe(viewLifecycleOwner) { result ->
            val isDeleted = result as? Boolean
            if (isDeleted == true) {
                // Mengubah warna ikon tombol favorit menjadi putih
                binding.btnFavorite.changeIconColor(R.color.white)
            }
        }

        // Menambahkan atau menghapus pengguna dari daftar favorit saat tombol favorit ditekan
        binding.btnFavorite.setOnClickListener {
            item?.let {
                viewModel.setFavorite(it)
            }
        }

        // Mengecek apakah pengguna sudah ada di daftar favorit saat fragment dibuka
        item?.id?.let { itemId ->
            viewModel.findFavorite(itemId) { result ->
                val isFavorite = result as? Boolean
                if (isFavorite == true) {
                    // Mengubah warna ikon tombol favorit menjadi merah
                    binding.btnFavorite.changeIconColor(R.color.red)
                } else {
                    // Mengubah warna ikon tombol favorit menjadi putih
                    binding.btnFavorite.changeIconColor(R.color.white)
                }
            }
        }
        return binding.root
    }

    private fun FloatingActionButton.changeIconColor(@ColorRes color: Int) {
        imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color))
    }
}