package com.example.pustaka.basic_api.ui.view

import AutoSliderAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pustaka.R
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.databinding.FragmentHomeBinding
import com.example.pustaka.basic_api.data.network.RetrofitInstance
import com.example.pustaka.basic_api.data.repository.BookRepository
import com.example.pustaka.basic_api.ui.adapter.BookListAdapter
import com.example.pustaka.basic_api.ui.viewModel.BookViewModel2
import com.example.pustaka.basic_api.utils.Resource
import com.example.pustaka.basic_api.utils.ViewModelFactory

class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookListAdapter

    private val bookViewModel: BookViewModel2 by activityViewModels {
        ViewModelFactory(BookViewModel2::class.java) {
            val repository = BookRepository(RetrofitInstance.getPerpusApi())
            BookViewModel2(repository)
        }
    }

    private val allBooks = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupAutoSlider()

        adapter = BookListAdapter(mutableListOf())
        binding.bookRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.bookRecyclerView.adapter = adapter

        getAllBooks()

        // Set up the filter buttons
        setupFilterButtons()

        // Set up search functionality
        setupSearchView()

        return binding.root
    }

    private fun setupAutoSlider() {
        val images = listOf(
            R.drawable.poster,   // Gambar manual
            R.drawable.poster2,
            R.drawable.poster3
        )
        binding.autoSlider.adapter = AutoSliderAdapter(images, binding.autoSlider)
        binding.wormIndicator.attachTo(binding.autoSlider) // Worm indicator untuk AutoSlider
    }

    private fun getAllBooks() {
        bookViewModel.getBooks(requireContext())
        bookViewModel.data.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Empty -> {
                    // Jika data kosong, tampilkan pesan "Tidak ada buku" dan sembunyikan RecyclerView
                    binding.bookRecyclerView.visibility = View.GONE
                    binding.bookEmpty.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    // Handle error jika perlu
                }
                is Resource.Loading -> {
                    // Handle loading jika perlu
                }
                is Resource.Success -> {
                    val books = resource.data?.map { book ->
                        Book(
                            book.id,
                            book.judul,
                            book.penulis,
                            book.kategori,
                            book.tanggal_terbit,
                            book.synopsis,
                            book.file_pdf,
                            book.cover,
                            book.createdAt,
                            book.updatedAt
                        )
                    }
                    if (books != null) {
                        allBooks.clear()
                        allBooks.addAll(books)
                        adapter.updateData(allBooks.toMutableList())

                        // Jika ada data, sembunyikan pesan "Tidak ada buku" dan tampilkan RecyclerView
                        if (allBooks.isNotEmpty()) {
                            binding.bookRecyclerView.visibility = View.VISIBLE
                            binding.bookEmpty.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun setupFilterButtons() {
        binding.btnComics.setOnClickListener {
            filterBooksByCategory("Comics")
            updateButtonStyles(binding.btnComics)
        }

        binding.btnEducation.setOnClickListener {
            filterBooksByCategory("Education")
            updateButtonStyles(binding.btnEducation)
        }

        binding.btnNovel.setOnClickListener {
            filterBooksByCategory("Novel")
            updateButtonStyles(binding.btnNovel)
        }

        // Optional: Add All button to show all books
        binding.btnAll.setOnClickListener {
            filterBooksByCategory("All")
            updateButtonStyles(binding.btnAll)
        }
    }

    private fun filterBooksByCategory(category: String) {
        val filteredBooks = if (category == "All") {
            allBooks
        } else {
            allBooks.filter { it.kategori == category }
        }

        // Update adapter dengan filtered books
        adapter.updateData(filteredBooks.toMutableList())

        // Cek apakah filteredBooks kosong
        if (filteredBooks.isEmpty()) {
            binding.bookRecyclerView.visibility = View.GONE
            binding.bookEmpty.visibility = View.VISIBLE // Tampilkan pesan "Tidak ada buku"
        } else {
            binding.bookRecyclerView.visibility = View.VISIBLE
            binding.bookEmpty.visibility = View.GONE // Sembunyikan pesan "Tidak ada buku"
        }
    }

    private fun updateButtonStyles(selectedButton: Button) {
        val buttons = listOf(binding.btnComics, binding.btnEducation, binding.btnNovel, binding.btnAll)
        buttons.forEach {
            it.setBackgroundResource(R.drawable.button_unselected)
        }
        selectedButton.setBackgroundResource(R.drawable.button_selected)
    }

    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Ketika pengguna mengirimkan query, Anda bisa melakukan tindakan tambahan jika perlu
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterBooksBySearchQuery(it)
                }
                return true
            }
        })
    }


    private fun filterBooksBySearchQuery(query: String) {
        val filteredBooks = allBooks.filter {
            it.judul.contains(query, ignoreCase = true) ||
                    it.penulis.contains(query, ignoreCase = true) ||
                    it.kategori.contains(query, ignoreCase = true)
        }

        // Update adapter dengan filtered books
        adapter.updateData(filteredBooks.toMutableList())

        // Cek apakah filteredBooks kosong
        if (filteredBooks.isEmpty()) {
            binding.bookRecyclerView.visibility = View.GONE
            binding.bookEmpty.visibility = View.VISIBLE // Tampilkan pesan "Tidak ada buku"
        } else {
            binding.bookRecyclerView.visibility = View.VISIBLE
            binding.bookEmpty.visibility = View.GONE // Sembunyikan pesan "Tidak ada buku"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
