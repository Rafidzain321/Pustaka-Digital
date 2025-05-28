package com.example.pustaka.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pustaka.basic_api.data.local.entity.BookEntity
import com.example.pustaka.basic_api.data.local.dao.BookDao
import com.example.pustaka.basic_api.utils.NetworkUtils
import com.example.pustaka.basic_api.utils.Resource
import kotlinx.coroutines.launch

class BookViewModel(private val bookDao: BookDao) : ViewModel() {

    // LiveData untuk buku dari database lokal
    private val _books = MutableLiveData<Resource<List<BookEntity>>>()
    val books: LiveData<Resource<List<BookEntity>>> = _books

    // LiveData untuk status tambah buku
    private val _createStatus = MutableLiveData<Resource<Unit>>()
    val createStatus: LiveData<Resource<Unit>> = _createStatus

    // LiveData untuk status hapus buku
    private val _deleteStatus = MutableLiveData<Resource<Unit>>()
    val deleteStatus: LiveData<Resource<Unit>> = _deleteStatus

    // Mengambil data buku dari database lokal
    fun getBooksLocal(forceRefresh: Boolean = false) {
        if (_books.value == null || forceRefresh) {
            viewModelScope.launch {
                try {
                    _books.value = Resource.Loading()
                    val response = bookDao.getAllBooks()
                    if (response.isEmpty()) {
                        _books.postValue(Resource.Empty("No books found"))
                    } else {
                        _books.postValue(Resource.Success(response))
                    }
                } catch (e: Exception) {
                    _books.postValue(Resource.Error("Error fetching books: ${e.message}"))
                }
            }
        }
    }

    // Menambahkan buku ke database lokal
    fun addBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                _createStatus.value = Resource.Loading()
                bookDao.insert(book)
                _createStatus.postValue(Resource.Success(Unit))
                getBooksLocal(forceRefresh = true) // Refresh daftar buku
            } catch (e: Exception) {
                _createStatus.postValue(Resource.Error("Error adding book: ${e.message}"))
            }
        }
    }

    // Menghapus buku dari database lokal
    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            try {
                _deleteStatus.value = Resource.Loading()
                bookDao.delete(book)
                _deleteStatus.postValue(Resource.Success(Unit))
                getBooksLocal(forceRefresh = true) // Refresh daftar buku
            } catch (e: Exception) {
                _deleteStatus.postValue(Resource.Error("Error deleting book: ${e.message}"))
            }
        }
    }
}
