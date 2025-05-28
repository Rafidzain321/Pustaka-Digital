package com.example.pustaka.basic_api.data.network

import com.example.pustaka.basic_api.data.model.Book
import com.example.pustaka.basic_api.data.model.FavoriteRequest
import com.example.pustaka.basic_api.data.model.FavoriteResponse
import com.example.pustaka.basic_api.data.model.ProductPostRequest
import com.example.pustaka.basic_api.data.model.ProductResponse
import com.example.pustaka.basic_api.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("product")
    suspend fun getProducts(@Header("Authorization") token: String): ProductResponse

    @POST("product")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body products: List<ProductPostRequest>
    ): ProductResponse

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Header("Authorization") token: String, @Path("id") id: String): Response<Unit>

    @GET("books")
    suspend fun getBooks() : List<Book>

    @Multipart
    @POST("books")
    suspend fun createBook(
        @Part("judul") judul: RequestBody,
        @Part("penulis") penulis: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("tanggal_terbit") tanggalTerbit: RequestBody,
        @Part("synopsis") synopsis: RequestBody,
        @Part file_pdf: MultipartBody.Part?,
        @Part cover: MultipartBody.Part?
    ): Book

    @DELETE("books/{id}")
    suspend fun deleteBook(
        @Path("id") id: Int
    ): Unit

    @POST("favorites")
    suspend fun addFavorite(
        @Body bookId : FavoriteRequest
    ) : Unit

    @GET("favorites")
    suspend fun getFavorites() : List<FavoriteResponse>

    @DELETE("favorites/{id}")
    suspend fun deleteFavorite(
        @Path("id") id: Int
    ): Unit
}