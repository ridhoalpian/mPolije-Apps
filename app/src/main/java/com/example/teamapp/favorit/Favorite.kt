package com.example.teamapp.favorit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamapp.R
import com.example.teamapp.database.DbModule
import com.example.teamapp.databinding.FragmentFavoriteBinding
import com.example.teamapp.detail.DetailViewModel
import com.example.teamapp.detail.Detaill
import com.example.teamapp.home.HomeViewModel
import com.example.teamapp.home.UserAdapter

class Favorite : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: UserAdapter

    // Membuat instance FavoriteViewModel dengan menggunakan Factory
    private val viewModel by viewModels<FavoriteViewModel>(){
        FavoriteViewModel.Factory(DbModule(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginisialisasi binding dan adapter
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = UserAdapter(mutableListOf()) { user ->
            // Menginisialisasi DetailFragment saat item daftar pengguna diklik
            val detailFragment = Detaill()
            val bundle = Bundle()
            bundle.putParcelable("item", user)
            detailFragment.arguments = bundle

            // Memulai transaksi fragment saat item diklik
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, detailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val view: View = binding.root

        // Mengatur layout manager dan adapter untuk RecyclerView
        binding.recycleViewFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewFav.adapter = adapter

        // Mengamati perubahan data dalam ViewModel
        viewModel.getUserFavorite().observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        return view
    }
}
