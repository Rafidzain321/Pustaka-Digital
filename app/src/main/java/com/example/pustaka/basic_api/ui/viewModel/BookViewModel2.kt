package com.example.pustaka.basic_api.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.model.FavoriteRequest
import com.example.pustaka.basic_api.data.model.FavoriteResponse
import com.example.pustaka.basic_api.data.repository.BookRepository
import com.example.pustaka.basic_api.utils.NetworkUtils
import com.example.pustaka.basic_api.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BookViewModel2(private val repository: BookRepository) :ViewModel() {
    private val _data = MutableLiveData<Resource<List<Book>>>()
    val data: LiveData<Resource<List<Book>>> = _data

    private val _createStatus = MutableLiveData<Resource<Book>>()
    val createStatus: LiveData<Resource<Book>> = _createStatus

    private val _deleteStatus = MutableLiveData<Resource<Unit>>()
    val deleteStatus: LiveData<Resource<Unit>> = _deleteStatus

    private val _addFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val addFavoriteStatus: LiveData<Resource<Unit>> = _addFavoriteStatus

    private val _removeFavoriteStatus = MutableLiveData<Resource<Unit>>()
    val removeFavoriteStatus: LiveData<Resource<Unit>> = _removeFavoriteStatus

    private val _favorites = MutableLiveData<Resource<List<FavoriteResponse>>>()
    val favorites: LiveData<Resource<List<FavoriteResponse>>> get() = _favorites

    fun getBooks(context: Context, forceRefresh: Boolean = false){
        if (_data.value == null || forceRefresh){
            if (NetworkUtils.isNetworkAvailable(context)){
                viewModelScope.launch {
                    try {
                        _data.value = Resource.Loading()
                        val response = repository.fetchBooks()
                        if (response.isEmpty()){
                            _data.postValue(Resource.Empty("No Data Found"))
                        } else {
                            _data.postValue(Resource.Success(response))
                        }
                    }catch (e: Exception){
                        _data.postValue(Resource.Error("Unknown Error : ${e.message}"))
                    }
                }
            } else {
                _data.postValue(Resource.Error("No Internet Connection"))
            }
        }
    }

    fun createBook(
        judul: RequestBody,
        penulis: RequestBody,
        kategori: RequestBody,
        tanggalTerbit: RequestBody,
        synopsis: RequestBody,
        filePdf: MultipartBody.Part,
        cover: MultipartBody.Part,
        context: Context
    ) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _createStatus.value = Resource.Loading()
                    val response = repository.createBook(
                        judul, penulis, kategori, tanggalTerbit, synopsis, filePdf, cover
                    )
                    _createStatus.postValue(Resource.Success(response))

                    getBooks(context, forceRefresh = true)

                } catch (e: Exception) {
                    _createStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        } else {
            _createStatus.postValue(Resource.Error("No internet connection"))
        }
    }

    fun deleteBook(context: Context, id : Int) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _deleteStatus.value = Resource.Loading()

                    val response = repository.deleteBook(id)

                    _deleteStatus.postValue(Resource.Success(response))

                    getBooks(context, forceRefresh = true)
                } catch (e: Exception) {
                    _deleteStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        } else {
            _deleteStatus.postValue(Resource.Error("No internet connection"))
        }
    }

    fun getFavoriteBook(context: Context, forceRefresh: Boolean = false){
        if (_favorites.value == null || forceRefresh){
            if (NetworkUtils.isNetworkAvailable(context)){
                viewModelScope.launch {
                    try {
                        _favorites.value = Resource.Loading()
                        val response = repository.fetchFavorite()
                        if (response.isEmpty()){
                            _favorites.postValue(Resource.Empty("No Data Found"))
                        } else {
                            _favorites.postValue(Resource.Success(response))
                        }
                    }catch (e: Exception){
                        _favorites.postValue(Resource.Error("Unknown Error : ${e.message}"))
                    }
                }
            } else {
                _favorites.postValue(Resource.Error("No Internet Connection"))
            }
        }
    }

    fun addToFavorites(context: Context, bookId: FavoriteRequest) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _addFavoriteStatus.value = Resource.Loading()
                    val response = repository.addFavorite(bookId)
                    _addFavoriteStatus.postValue(Resource.Success(response))
                    getFavoriteBook(context, forceRefresh = true)
                } catch (e: Exception) {
                    _addFavoriteStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        } else {
            _addFavoriteStatus.postValue(Resource.Error("No internet connection"))
        }
    }

    fun removeFromFavorites(context: Context, bookId: Int) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            viewModelScope.launch {
                try {
                    _removeFavoriteStatus.value = Resource.Loading()

                    val response = repository.deleteFavorite(bookId)

                    _removeFavoriteStatus.postValue(Resource.Success(response))
                    getBooks(context, forceRefresh = true)
                } catch (e: Exception) {
                    _removeFavoriteStatus.postValue(Resource.Error("Unknown error: ${e.message}"))
                }
            }
        } else {
            _removeFavoriteStatus.postValue(Resource.Error("No internet connection"))
        }
    }
}