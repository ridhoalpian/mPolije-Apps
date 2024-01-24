package com.example.teamapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.teamapp.databinding.FragmentHomeItemBinding
import com.example.teamapp.model.ResponseUserGithub
import java.util.*

class UserAdapter(
    private var fullData: MutableList<ResponseUserGithub.Item>, // List data pengguna GitHub lengkap
    private val listener: (ResponseUserGithub.Item) -> Unit // Listener untuk item yang diklik
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    private var filteredData: MutableList<ResponseUserGithub.Item> = fullData.toMutableList() // Inisialisasi data yang difilter dengan data awal

    init {
        this.fullData = fullData.toMutableList() // Salin data awal ke fullData
        this.filteredData = fullData.toMutableList() // Salin data awal ke filteredData
    }

    // Fungsi ini digunakan untuk mengganti data pada adapter dengan data baru.
    fun setData(data: MutableList<ResponseUserGithub.Item>) {
        this.fullData.clear() // Kosongkan data lengkap
        this.fullData.addAll(data) // Tambahkan data baru ke data lengkap
        this.filteredData = fullData.toMutableList() // Salin data baru ke data yang difilter
        notifyDataSetChanged() // Notifikasi adapter bahwa data telah berubah
    }

    // Kelas ViewHolder untuk mengatur tampilan item pengguna
    class UserViewHolder(private val binding: FragmentHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseUserGithub.Item) {
            // Menggunakan Glide untuk mengisi gambar pengguna
            Glide.with(binding.itemImage)
                .load(item.avatar_url)
                .circleCrop()
                .into(binding.itemImage)

            binding.itemContent.text = item.url
            binding.itemContent2.text = item.login
        }
    }

    // Fungsi ini digunakan untuk membuat ViewHolder baru saat tampilan item dibuat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(FragmentHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    // Fungsi ini digunakan untuk mengisi tampilan item dengan data pengguna
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = filteredData[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item) // Menangani klik item dan memanggil listener
        }
    }

    // Fungsi ini mengembalikan jumlah item dalam adapter
    override fun getItemCount(): Int = filteredData.size

    // Fungsi ini mengimplementasikan filter untuk mencari pengguna berdasarkan kata kunci
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charSearch = constraint.toString().toLowerCase(Locale.ROOT)

                if (charSearch.isEmpty()) {
                    filteredData = fullData.toMutableList() // Jika kata kunci kosong, tampilkan semua data
                } else {
                    val resultList = mutableListOf<ResponseUserGithub.Item>()
                    for (row in fullData) {
                        if (row.login.toLowerCase(Locale.ROOT).contains(charSearch)) {
                            resultList.add(row) // Tambahkan item yang sesuai dengan kata kunci ke hasil
                        }
                    }
                    filteredData = resultList // Setel data yang difilter ke hasil pencarian
                }

                val results = FilterResults()
                results.values = filteredData
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as? MutableList<ResponseUserGithub.Item> ?: fullData
                notifyDataSetChanged() // Notifikasi adapter bahwa data hasil pencarian telah berubah
            }
        }
    }
}
