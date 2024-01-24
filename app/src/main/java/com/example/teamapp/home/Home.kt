package com.example.teamapp.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.teamapp.utils.Result
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamapp.R
import com.example.teamapp.databinding.FragmentHomeBinding
import com.example.teamapp.detail.Detaill
import com.example.teamapp.model.ResponseUserGithub

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userList: MutableList<ResponseUserGithub.Item>

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        adapter = UserAdapter(mutableListOf()) { user ->
            // Membuat instance DetailFragment saat item daftar pengguna diklik
            val detailFragment = Detaill()
            val bundle = Bundle()
            bundle.putParcelable("item", user)
            detailFragment.arguments = bundle

            // Memulai transaksi fragment untuk menampilkan DetailFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, detailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

// Mengatur LinearLayoutManager dan adapter untuk RecyclerView
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.adapter = adapter

// Mengamati hasil pengambilan daftar pengguna dari ViewModel
        viewModel.resultUser.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success<*> -> {
                    // Jika pengambilan data berhasil, mengisi adapter dengan data pengguna
                    userList = result.data as MutableList<ResponseUserGithub.Item>
                    adapter.setData(userList)
                    setupSearchView()
                }

                is Result.Error -> {
                    // Jika terjadi kesalahan, menampilkan pesan kesalahan
                    Toast.makeText(
                        requireContext(),
                        result.exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {
                    // Menampilkan atau menyembunyikan progressBar sesuai dengan status loading
                    binding.progressBar.isVisible = result.isLoading
                }
            }
        }

// Memanggil metode untuk mengambil daftar pengguna dari ViewModel
        viewModel.getUser()

        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Memfilter daftar pengguna berdasarkan teks pencarian
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}

