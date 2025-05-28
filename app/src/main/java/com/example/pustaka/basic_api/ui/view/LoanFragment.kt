package com.example.pustaka.basic_api.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.model.FavoriteResponse
import com.example.pustaka.basic_api.data.network.RetrofitInstance
import com.example.pustaka.basic_api.data.repository.BookRepository
import com.example.pustaka.basic_api.ui.adapter.BookListAdapter
import com.example.pustaka.basic_api.ui.adapter.FavoriteAdapter
import com.example.pustaka.basic_api.ui.viewModel.BookViewModel2
import com.example.pustaka.basic_api.utils.Resource
import com.example.pustaka.basic_api.utils.ViewModelFactory
import com.example.pustaka.databinding.FragmentLoanBinding

class LoanFragment : Fragment() {

    private var _binding: FragmentLoanBinding? = null
    private val binding get() = _binding!!

    val bookViewModel: BookViewModel2 by activityViewModels {
        ViewModelFactory(BookViewModel2::class.java) {
            val repository = BookRepository(RetrofitInstance.getPerpusApi())
            BookViewModel2(repository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoanBinding.inflate(inflater, container, false)

        setupBooks(binding)
        return binding.root
    }

    private fun setupBooks(binding: FragmentLoanBinding) {
        val adapter = FavoriteAdapter(mutableListOf())
        bookViewModel.getFavoriteBook(requireActivity())
        bookViewModel.favorites.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val books = resource.data?.map { favoriteResponse ->
                        FavoriteResponse(
                            id = favoriteResponse.id,
                            bookId = favoriteResponse.bookId,
                            createdAt = favoriteResponse.createdAt,
                            updatedAt = favoriteResponse.updatedAt,
                            Book = favoriteResponse.Book
                        )
                    }
                    books?.let {
                        adapter.updateData(it.toMutableList())
                    }
                }

                is Resource.Empty -> {}
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }

        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}