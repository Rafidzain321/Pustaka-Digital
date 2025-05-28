package com.example.pustaka.basic_api.data.repository

import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.model.FavoriteRequest
import com.example.pustaka.basic_api.data.model.FavoriteResponse
import com.example.pustaka.basic_api.data.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BookRepository(private val api: ApiService) {
    suspend fun fetchBooks(): List<Book> {
        return api.getBooks()
    }
    suspend fun createBook(
        judul: RequestBody,
        penulis: RequestBody,
        kategori: RequestBody,
        tanggalTerbit: RequestBody,
        synopsis: RequestBody,
        filePdf: MultipartBody.Part,
        cover: MultipartBody.Part
    ): Book {
        return api.createBook(judul, penulis, kategori, tanggalTerbit, synopsis, filePdf, cover)
    }

    suspend fun deleteBook(id : Int){
        return api.deleteBook(id)
    }

    suspend fun addFavorite(id : FavoriteRequest){
        return api.addFavorite(id)
    }

    suspend fun fetchFavorite() : List<FavoriteResponse>{
        return api.getFavorites()
    }

    suspend fun deleteFavorite(id : Int){
        return api.deleteFavorite(id)
    }
}
